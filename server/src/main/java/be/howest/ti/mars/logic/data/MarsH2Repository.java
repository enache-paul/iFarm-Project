package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.Constants;
import be.howest.ti.mars.logic.domain.Garden;
import be.howest.ti.mars.logic.domain.Plant;
import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.exceptions.RepositoryException;
import org.h2.tools.Server;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
This is only a starter class to use an H2 database.
In this start project there was no need for a Java interface MarsRepository.
Please always use interfaces when needed.

To make this class useful, please complete it with the topics seen in the module OOA & SD
 */

public class MarsH2Repository {
    private static final Logger LOGGER = Logger.getLogger(MarsH2Repository.class.getName());
    private static final String SQL_PLANT_BY_ID = "select id, name, imgLink from plants where id = ?";
    private static final String SQL_INSERT_PLANT = "insert into plants (name, imgLink) values (?,?)";
    private static final String SQL_GARDEN_BY_ID = "select id, name, tier from gardens where id = ?";
    private static final String SQL_INSERT_OWNEDPLANT = "insert into ownedPlants (plantId, gardenId, status) "
                                                        + "values (?, ?, ?)";
    private static final String SQL_FORUMPOSTS_BY_GARDEN_ID = "select id, gardenId, text, imgLink, uploadTime from " +
                                                        "forumPosts where gardenId = ?";
    private static final String SQL_FORUMPOSTS_RECENT = "select id, gardenId, text, imgLink, uploadTime from " +
                                                        "forumPosts order by uploadTime desc limit 10;";
    private static final String SQL_OWNEDPLANTS_BY_GARDEN_ID = "select id, plantId, gardenId, status from " +
                                                               "ownedPlants where gardenId = ?";
    private static final String SQL_SCANS_BY_OWNEDPLANTS = "select ownedPlantId, scanTime, fertilizerLevel, waterLevel, " +
                                                            "expectedYield, health, growth from scans where ownedPlantId = ?;";
    private static final String SQL_INSERT_SCAN = "insert into scans (ownedPlantId, scanTime, fertilizerLevel, waterLevel, "  +
                                                    "expectedYield, health, growth) values (?, ?, ?, ?, ?, ?, ?);";
    private static final String SQL_OWNEDPLANT_BY_ID = "select id, plantId, gardenId, status from ownedPlants where id = ?;";
    private static final String SQL_FORUM_POST_BY_ID = "select * from forumposts where id = ?;";
    private static final String SQL_INSERT_FORUM_POST = "insert into forumPosts (gardenId, text, imgLink, uploadTime) values (?,?,?,?);";
    private static final String IMG_LINK = "imgLink";
    private static final String SQL_ALL_PLANTS = "select id, name, imgLink from plants;";
    private static final String SQL_UPDATE_GARDEN_TIER = "update gardens set tier = ? where id = ?;";
    private static final String SQL_ALL_GARDENS = "select id, name, tier from gardens;";
    private static final String SQL_LIKES_FROM_POSTS = "select count(*) as likes from likedPosts where postId = ?;";
    private static final String SQL_LIKES_FROM_COMMENTS = "select count(*) as likes from likedComments where commentId = ?;";
    private static final String SQL_COMMENTS = "select id, postId, gardenId, text from comments where postId = ?;";
    private static final String SQL_INSERT_GARDEN = "insert into gardens (name, tier) values (?, ?);";
    private static final String SQL_INSERT_POST_LIKES = "insert into likedPosts (postId, gardenId) values (?, ?);";
    private static final String SQL_INSERT_COMMENT_LIKES = "insert into likedComments (commentId, gardenId) values (?, ?);";
    private static final String SQL_GARDEN_ID_BY_COMMENT = "select gardenId from comments where id = ?;";
    private static final String SQL_INSERT_COMMENT = "insert into comments (postId, gardenId, text) values (?, ?, ?);";
    private static final String SQL_DELETE_OWNED_PLANT = "delete from scans where ownedPlantId = ?; delete from ownedPlants where id = ?;";
    private static final String SQL_INSERT_ROBOT = "INSERT INTO robots (name, gardenId, description, batteryLevel) VALUES (?, ?, ?, 97);";
    private static final String SQL_ROBOTS_BY_GARDEN_ID = "select * from robots where gardenid = ?";
    private static final String SPEC_GARDEN_ID = "gardenId";
    private static final int BATTERY_LEVEL_ROBOT = 97;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private final Server dbWebConsole;
    private final String username;
    private final String password;
    private final String url;

    public MarsH2Repository(String url, String username, String password, int console) {
        try {
            this.username = username;
            this.password = password;
            this.url = url;
            this.dbWebConsole = Server.createWebServer(
                    "-ifNotExists",
                    "-webPort", String.valueOf(console)).start();
            LOGGER.log(Level.INFO, "Database web console started on port: {0}", console);
            this.generateData();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "DB configuration failed", ex);
            throw new RepositoryException("Could not configure MarsH2repository");
        }
    }

    public void cleanUp() {
        if (dbWebConsole != null && dbWebConsole.isRunning(false))
            dbWebConsole.stop();

        try {
            Files.deleteIfExists(Path.of("./db-23.mv.db"));
            Files.deleteIfExists(Path.of("./db-23.trace.db"));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Database cleanup failed.", e);
            throw new RepositoryException("Database cleanup failed.");
        }
    }

    public void generateData() {
        try {
            executeScript("db-create.sql");
            executeScript("db-populate.sql");
        } catch (IOException | SQLException ex) {
            LOGGER.log(Level.SEVERE, "Execution of database scripts failed.", ex);
        }
    }

    private void executeScript(String fileName) throws IOException, SQLException {
        String createDbSql = readFile(fileName);
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(createDbSql)
        ) {
            stmt.executeUpdate();
        }
    }

    private String readFile(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null)
            throw new RepositoryException("Could not read file: " + fileName);

        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public Plant insertPlant(String plantName, String imgLink) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_PLANT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, plantName);
            stmt.setString(2, imgLink);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating plant failed, no rows affected.");
            }

            Plant plant = new Plant(plantName, imgLink);
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    plant.setId(generatedKeys.getInt(1));
                    return plant;
                }
                else {
                    throw new SQLException("Creating plant failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to create plant.", ex);
            throw new RepositoryException("Could not create plant.");
        }
    }

    public ForumPost getForumPost(int forumPostId) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_FORUM_POST_BY_ID)
        ) {
            stmt.setInt(1, forumPostId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    java.util.Date uploadTime = dateFormat.parse(rs.getString("uploadtime"));
                    Garden garden = getGarden(rs.getInt("gardenid"));
                    String text = rs.getString("text");
                    String imgLink = rs.getString(IMG_LINK);
                    int likes = getLikes(forumPostId, SQL_LIKES_FROM_POSTS);
                    List<Comment> comments = getComments(forumPostId);


                    return new ForumPost(forumPostId, garden, text, imgLink, uploadTime, likes, comments);
                } else {
                    return null;
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "failed to get forum post", ex);
            throw new RepositoryException("Could not get forum post");
        }
    }

    private List<OwnedPlant> getOwnedPlantsByGarden(int gardenId) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_OWNEDPLANTS_BY_GARDEN_ID)
        ) {
            List<OwnedPlant> ownedPlants = new ArrayList<>();

            stmt.setInt(1, gardenId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    Plant plant = getPlant(rs.getInt("plantId"));
                    Constants.Status status = Constants.Status.valueOf(rs.getString("status"));
                    List<Scan> scans = getScansByOwnedPlant(id);

                    ownedPlants.add(new OwnedPlant(id, plant, status, scans));
                }
            }

            return ownedPlants;

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "failed to get owned plants", ex);
            throw new RepositoryException("Could not get owned plants");
        }
    }

    public List<Scan> getScansByOwnedPlant(int ownedPlantId) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_SCANS_BY_OWNEDPLANTS)
        ) {
            List<Scan> scans = new ArrayList<>();

            stmt.setInt(1, ownedPlantId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    java.util.Date scanTime = dateFormat.parse(rs.getString("scanTime"));
                    int fertilizerLevel = rs.getInt("fertilizerLevel");
                    int waterLevel = rs.getInt("waterLevel");
                    int expectedYield = rs.getInt("expectedYield");
                    int health = rs.getInt("health");
                    int growth = rs.getInt("growth");

                    scans.add(new Scan(scanTime, fertilizerLevel, waterLevel, expectedYield, health, growth));
                }
            }

            return scans;

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "failed to get scans", ex);
            throw new RepositoryException("Could not get scans");
        }
    }

    private List<ForumPost> getForumPostsByGarden(int gardenId) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_FORUMPOSTS_BY_GARDEN_ID)
        ) {
            stmt.setInt(1, gardenId);
            return postsFromStmt(stmt);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "failed to get forum posts", ex);
            throw new RepositoryException("could not get forum posts");
        }
    }

    public List<ForumPost> getRecentForumPosts() {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_FORUMPOSTS_RECENT)
        ) {

            return postsFromStmt(stmt);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "failed to get forum posts", ex);
            throw new RepositoryException("could not get forum posts");
        }
    }

    private List<ForumPost> postsFromStmt(PreparedStatement stmt) {
        List<ForumPost> posts = new ArrayList<>();

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                Garden garden = getBasicGarden(rs.getInt(SPEC_GARDEN_ID));
                String text = rs.getString("text");
                String imgLink = rs.getString(IMG_LINK);
                java.util.Date uploadTime = dateFormat.parse(rs.getString("uploadtime"));
                int likes = getLikes(id, SQL_LIKES_FROM_POSTS);
                List<Comment> comments = getComments(id);

                posts.add(new ForumPost(id, garden, text, imgLink, uploadTime, likes, comments));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "failed to retrieve forum posts", ex);
            throw new RepositoryException("failed to retrieve forum posts");
        }

        return posts;
    }

    public OwnedPlant insertOwnedPlant(int plantId, int gardenId, Constants.Status status) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_OWNEDPLANT, Statement.RETURN_GENERATED_KEYS)
        ) {

            stmt.setString(1, "" + plantId);
            stmt.setString(2, "" + gardenId);
            stmt.setString(3, status.toString());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating ownedPlant failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    Plant plant = getPlant(plantId);
                    Garden garden = getBasicGarden(gardenId);
                    return new OwnedPlant(id, plant, garden, status);
                }
                else {
                    throw new SQLException("Creating ownedPlant failed, no ID obtained.");
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to create ownedPlant.", ex);
            throw new RepositoryException("Could not create ownedPlant.");
        }
    }

    public ForumPost insertForumPost(int gardenId, String text, String imgLink, java.util.Date uploadTime) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_FORUM_POST, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, gardenId);
            stmt.setString(2, text);
            stmt.setString(3, imgLink);
            stmt.setString(4, dateFormat.format(uploadTime));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating forum post failed, no rows affected.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    Garden garden = getBasicGarden(gardenId);

                    return new ForumPost(id, garden, text, imgLink, uploadTime, 0, new ArrayList<>());
                }
                else {
                    throw new SQLException("Creating forum post failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to create forum post.", ex);
            throw new RepositoryException("Could not create forum post.");
        }
    }

    public Scan insertScan(int ownedPlantId, java.util.Date scanTime, int fertilizerLevel, int waterLevel,
                           int expectedYield, int health, int growth)   {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_SCAN, Statement.RETURN_GENERATED_KEYS)
        ) {

            stmt.setInt(1, ownedPlantId);
            stmt.setString(2, dateFormat.format(scanTime));
            stmt.setInt(3, fertilizerLevel);
            stmt.setInt(4, expectedYield);
            stmt.setInt(5, waterLevel);
            stmt.setInt(6, health);
            stmt.setInt(7, growth);


            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating scan failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    OwnedPlant plant = getOwnedPlantByScan(ownedPlantId);
                    return new Scan(plant, scanTime, fertilizerLevel, waterLevel, expectedYield, health, growth);
                }
                else {
                    throw new SQLException("Creating Scan failed.");
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to create Scan.", ex);
            throw new RepositoryException("Could not create Scan.");
        }
    }

    private OwnedPlant getOwnedPlantByScan(int id) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_OWNEDPLANT_BY_ID)
        ) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Plant plant = getPlant(rs.getInt("plantId"));
                    Constants.Status status =  Constants.Status.valueOf(rs.getString("status"));

                    return new OwnedPlant(id, plant, status);
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "failed to get ownedPlant", ex);
            throw new RepositoryException("Could not get ownedPlant");
        }
    }

    public Plant getPlant(int plantId) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_PLANT_BY_ID)
        ) {
            stmt.setInt(1, plantId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Plant(rs.getInt("id"), rs.getString("name"), rs.getString(IMG_LINK));
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get plant.", ex);
            throw new RepositoryException("Could not get plant.");
        }
    }

    public List<Plant> getAllPlants() {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_ALL_PLANTS);
                ResultSet rs = stmt.executeQuery()
        ){
            List<Plant> plantList = new ArrayList<>();
            while (rs.next()) {
                plantList.add(new Plant(rs.getInt("id"), rs.getString("name"), rs.getString(IMG_LINK)));
            }

            return plantList;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "failed to get all plants", ex);
            throw new RepositoryException("Failed to get all plants");
        }
    }

    public Garden insertGarden(String name, Constants.Tier tier) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_GARDEN, Statement.RETURN_GENERATED_KEYS)
        ) {

            stmt.setString(1, name);
            stmt.setString(2, tier.toString());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating ownedPlant failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    return new Garden(id, name, tier);
                }
                else {
                    throw new SQLException("Creating garden failed, no ID obtained.");
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to create garden.", ex);
            throw new RepositoryException("Could not create garden.");
        }
    }

    public Garden getGarden(int id) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_GARDEN_BY_ID)
        ) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return getGardenFromRs(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "failed to get garden", ex);
            throw new RepositoryException("Could not get garden");
        }
    }

    public List<Garden> getAllGardens() {
        try (
                Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL_ALL_GARDENS);
        ResultSet rs = stmt.executeQuery()
        ){
            List<Garden> gardens = new ArrayList<>();
            while (rs.next()) {
                gardens.add(getGardenFromRs(rs));
            }

            return gardens;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "failed to get all owned plants", ex);
            throw new RepositoryException("Failed to get all owned plants");
        }
    }

    public Garden updateGardenTier(int gardenId, Constants.Tier tier) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_GARDEN_TIER)) {

            stmt.setString(1, tier.toString());
            stmt.setInt(2, gardenId);
            stmt.executeUpdate();

            return getGarden(gardenId);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to update garden tier.", ex);
            throw new RepositoryException("Could not update garden tier.");
        }
    }

    private Garden getGardenFromRs(ResultSet rs) {
        try {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            Constants.Tier tier = Constants.Tier.valueOf(rs.getString("tier"));
            List<ForumPost> posts = getForumPostsByGarden(id);
            List<OwnedPlant> ownedPlants = getOwnedPlantsByGarden(id);
            List<MarketplaceAd> marketplaceAds = new ArrayList<>(); // get actual list from database
            List<Robot> robots = getOwnedRobotByGardenId(id);

            return new Garden(id, name, tier, posts, ownedPlants, robots, marketplaceAds);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to get Garden from result set", ex);
            throw new RepositoryException("Failed to get Garden from result set");
        }
    }

    public int getLikes(int id, String sql) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            int likes = 0;

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    likes = rs.getInt("likes");
                }
            }

            return likes;

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "failed to get like count", ex);
            throw new RepositoryException("could not get like count");
        }
    }

    public List<Comment> getComments(int postId) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_COMMENTS)
        ) {
            List<Comment> comments = new ArrayList<>();

            stmt.setInt(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int gardenId = rs.getInt(SPEC_GARDEN_ID);
                    Garden garden = getBasicGarden(gardenId);
                    String text = rs.getString("text");
                    int likes = getLikes(id, SQL_LIKES_FROM_COMMENTS);
                    comments.add(new Comment(id, garden, text, likes));
                }
            }

            return comments;

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "failed to get like count for post", ex);
            throw new RepositoryException("could not get like count for postf");
        }
    }

    public Garden getBasicGarden(int id) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_GARDEN_BY_ID)
        ) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    Constants.Tier tier = Constants.Tier.valueOf(rs.getString("tier"));

                    return new Garden(id, name, tier);
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "failed to get garden", ex);
            throw new RepositoryException("Could not get garden");
        }
    }

    public void insertPostLike(int postId, int gardenId) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_POST_LIKES, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setInt(1, postId);
            stmt.setInt(2, gardenId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                LOGGER.log(Level.WARNING, "This garden already liked this post");
                throw new SQLException("inserting post like failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new SQLException("inserting like failed, no ID obtained.");
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to create like.", ex);
            throw new RepositoryException("Could not create like.");
        }
    }

    public void insertCommentLike(int commentId, int gardenId) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_COMMENT_LIKES, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setInt(1, commentId);
            stmt.setInt(2, gardenId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                LOGGER.log(Level.WARNING, "This garden already liked this comment");
                throw new SQLException("inserting comment like failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new SQLException("inserting like failed, no ID obtained.");
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to create like.", ex);
            throw new RepositoryException("Could not create like.");
        }
    }

    public int getCommentGardenId(int commentId) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_GARDEN_ID_BY_COMMENT)
        ) {
            stmt.setInt(1, commentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(SPEC_GARDEN_ID);
                } else {
                    throw new SQLException("failed to get gardenId");
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "failed to get garden id", ex);
            throw new RepositoryException("Could not get garden id");
        }
    }

    public Comment insertComment(int postId, int gardenId, String text) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_COMMENT, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setInt(1, postId);
            stmt.setInt(2, gardenId);
            stmt.setString(3, text);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("inserting comment, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    return new Comment(id, getBasicGarden(gardenId), text, 0);
                }
                else  {
                    throw new SQLException("inserting comment, no ID obtained.");
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to create comment.", ex);
            throw new RepositoryException("Could not create comment.");
        }
    }

    public Robot insertRobot(String robotName, int gardenId, String description) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_ROBOT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, robotName);
            stmt.setInt(2, gardenId);
            stmt.setString(3, description);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating robot failed, no rows affected.");
            }

            Robot robot = new Robot(robotName, gardenId, description, BATTERY_LEVEL_ROBOT);
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return robot;
                }
                else {
                    throw new SQLException("Creating robot failed.");
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to create robot.", ex);
            throw new RepositoryException("Could not create robot.");
        }
    }

    private List<Robot> getOwnedRobotByGardenId(int gardenId) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_ROBOTS_BY_GARDEN_ID)
        ) {
            List<Robot> ownedRobots = new ArrayList<>();

            stmt.setInt(1, gardenId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String gardenName = rs.getString("name");
                    String description = rs.getString("description");
                    int battery = rs.getInt("batterylevel");

                    ownedRobots.add(new Robot(gardenName, description, battery));
                }
            }

            return ownedRobots;

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "failed to get owned robots", ex);
            throw new RepositoryException("Could not get owned robots");
        }
    }

    public void harvest(int ownedPlantId) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE_OWNED_PLANT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, ownedPlantId);
            stmt.setInt(2, ownedPlantId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting owned plant failed, no rows affected.");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to delete owned plant.", ex);
            throw new RepositoryException("Could not delete owned plant.");
        }
    }
}