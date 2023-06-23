-- default plants --
INSERT INTO plants (name, imgLink) VALUES ('avocado', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('broccoli', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('cauliflower', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('daikon', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('eggplant', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('fennel', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('green beans', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('habanero', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('iceberg lettuce', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('jicama', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('kale', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('leek', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('mushroom', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('nasturtium', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('onion', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('pepper', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('quandong', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('radish', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('spinash', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('tomato', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('upland cress', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('vanilla', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('water chestnuts', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('yam', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('zucchini', 'https:example.com');

INSERT INTO plants (name, imgLink) VALUES ('apple', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('banana', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('cherry', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('dragon fruit', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('elderberry', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('fig', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('grape', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('honeydew melon', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('imbe', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('juniper berry', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('kiwi', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('lemon', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('mango', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('nectarine', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('orange', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('pineapple', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('quince', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('raspberry', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('strawberry', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('tangerine', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('ugli', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('victoria plum', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('watermelon', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('yellow passion fruit', 'https:example.com');
INSERT INTO plants (name, imgLink) VALUES ('zhe', 'https:example.com');

-- dummy data --
-- gardens
INSERT INTO gardens (name, tier) VALUES ('Johns epic garden','FREE');
INSERT INTO gardens (name, tier) VALUES ('mom', 'PREMIUM');
-- forum posts
INSERT INTO forumPosts (gardenId, text, imgLink, uploadTime) VALUES (1, 'this gonna be tasty', 'https://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/Hjortron.jpg/330px-Hjortron.jpg', '1200-01-02 00:00:00');
INSERT INTO forumPosts (gardenId, text, imgLink, uploadTime) VALUES (1, 'omg this is the best application in history', 'https://upload.wikimedia.org/wikipedia/commons/thumb/5/54/Sapodilla_cut_open_inside_flesh.jpg/330px-Sapodilla_cut_open_inside_flesh.jpg', '1500-01-02 00:00:00');
INSERT INTO forumposts (gardenId, text, imgLink, uploadTime) VALUES (2, 'cool tomato','https://upload.wikimedia.org/wikipedia/commons/thumb/f/f9/Diospyros_digyna_2.jpg/180px-Diospyros_digyna_2.jpg','1999-05-01 00:00:00');
-- owned plants
INSERT INTO ownedPlants (plantId, gardenId, Status) VALUES (1, 1, 'FUTURE');
INSERT INTO ownedPlants (plantId, gardenId, Status) VALUES (5, 1, 'FUTURE');
-- scans
INSERT INTO scans (ownedPlantId, scanTime, fertilizerLevel, waterLevel, expectedYield, health, growth)
VALUES (1, '1200-01-02 00:00:00', 90, 89, 40, 29, 48);
-- robots
INSERT INTO robots (name, gardenId, description, batteryLevel) VALUES ('henk', 1, 'waters plants', 50);
-- market place ads
INSERT INTO marketplaceAds (gardenId, plantId, description, quantity, status, price, imgLink, uploadTime) VALUES
    (1, 1, 'buy my fud', 2, 'LIVE', 20, 'https:example.com', '1200-01-02 00:00:00');
-- post likes

-- comments
INSERT INTO comments (postId, gardenId, text) VALUES (1, 1, 'nice'), (1, 2, 'bruh moment');
-- comment likes
INSERT INTO likedComments (commentId, gardenid) VALUES (1, 2), (1, 1);

