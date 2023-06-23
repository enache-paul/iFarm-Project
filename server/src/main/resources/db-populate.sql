-- default plants --
INSERT INTO plants (name, imgLink) VALUES ('avocado', 'https://images.pexels.com/photos/2228553/pexels-photo-2228553.jpeg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('broccoli', 'https://images.pexels.com/photos/1359326/pexels-photo-1359326.jpeg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('cauliflower', 'https://images.pexels.com/photos/4963458/pexels-photo-4963458.jpeg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('daikon', 'https://images.pexels.com/photos/7214623/pexels-photo-7214623.jpeg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('eggplant', 'https://images.pexels.com/photos/2694393/pexels-photo-2694393.jpeg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('fennel', 'https://images.pexels.com/photos/159357/fennel-vegetables-fennel-bulb-food-159357.jpeg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('green beans', 'https://images.pexels.com/photos/3004798/pexels-photo-3004798.jpeg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('habanero', 'https://images.pexels.com/photos/7829662/pexels-photo-7829662.jpeg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('iceberg lettuce', 'https://images.pexels.com/photos/1199562/pexels-photo-1199562.jpeg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('jicama', 'https://thumbs.dreamstime.com/b/jicama-isolated-white-background-39421645.jpg');
INSERT INTO plants (name, imgLink) VALUES ('kale', 'https://images.pexels.com/photos/51372/kale-vegetables-brassica-oleracea-var-sabellica-l-51372.jpeg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('leek', 'https://images.pexels.com/photos/50585/knife-kitchen-cook-wooden-board-50585.jpeg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('mushroom', 'https://images.pexels.com/photos/36438/mushrooms-brown-mushrooms-cook-eat.jpg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('nasturtium', 'https://images.pexels.com/photos/5394410/pexels-photo-5394410.jpeg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('onion', 'https://images.pexels.com/photos/533342/pexels-photo-533342.jpeg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('pepper', 'https://images.pexels.com/photos/128536/pexels-photo-128536.jpeg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('quandong', 'https://thumbs.dreamstime.com/z/jicama-isolated-white-background-39421645.jpg');
INSERT INTO plants (name, imgLink) VALUES ('radish', 'https://images.pexels.com/photos/191043/pexels-photo-191043.jpeg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('spinash', 'https://images.pexels.com/photos/2325843/pexels-photo-2325843.jpeg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('tomato', 'https://images.pexels.com/photos/1327838/pexels-photo-1327838.jpeg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('upland cress', 'https://images.pexels.com/photos/3296644/pexels-photo-3296644.jpeg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('vanilla', 'https://images.pexels.com/photos/14381803/pexels-photo-14381803.jpeg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('water chestnuts', 'https://images.pexels.com/photos/7740628/pexels-photo-7740628.png?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('yam', 'https://images.pexels.com/photos/7543152/pexels-photo-7543152.jpeg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('zucchini', 'https://images.pexels.com/photos/128420/pexels-photo-128420.jpeg?auto=compress&cs=tinysrgb&w=600');

INSERT INTO plants (name, imgLink) VALUES ('apple', 'https://images.pexels.com/photos/102104/pexels-photo-102104.jpeg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('banana', 'https://images.pexels.com/photos/1093038/pexels-photo-1093038.jpeg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('cherry', 'https://images.pexels.com/photos/35629/bing-cherries-ripe-red-fruit.jpg?auto=compress&cs=tinysrgb&w=600');
INSERT INTO plants (name, imgLink) VALUES ('dragon fruit', 'https://images.pexels.com/photos/162926/dragon-fruit-passion-fruit-southern-countries-exotic-162926.jpeg?auto=compress&cs=tinysrgb&w=600');

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

