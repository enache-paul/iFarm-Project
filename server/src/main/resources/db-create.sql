drop table if exists likedPosts;
drop table if exists likedComments;
drop table if exists comments;
drop table if exists marketplaceAds;
drop table if exists robots;
drop table if exists scans;
drop table if exists ownedPlants;
drop table if exists forumPosts;
drop table if exists gardens;
drop table if exists plants;

create table gardens
(
    id      int not null auto_increment constraint pk_gardens primary key,
    name    varchar(128) not null,
    tier    varchar(128) not null
);
create table plants
(
    id        int not null auto_increment constraint pk_plants primary key,
    name      varchar(255) not null,
    imgLink   varchar(512) not null
);
create table ownedPlants
(
    id        int not null auto_increment constraint pk_ownedPlants primary key,
    plantId   int not null references plants(id),
    gardenId  int not null references gardens(id),
    status    varchar(128) not null
);
create table scans
(
    ownedPlantId    int not null references ownedPlants(id),
    scanTime        varchar(100) not null constraint pk_scans primary key,
    fertilizerLevel int not null,
    waterLevel      int not null,
    expectedYield   int not null,
    health          int not null,
    growth          int not null
);
create table forumPosts
(
    id           int not null auto_increment constraint pk_forumPosts primary key,
    gardenId     int not null references gardens(id),
    text         varchar(1024),
    imgLink      varchar(512),
    uploadTime   varchar(512) not null
);
create table marketplaceAds
(
    id             int not null auto_increment constraint pk_marketplaceAds primary key,
    gardenId       int not null references gardens(id),
    plantId        int not null references plants(id),
    description    varchar(1024),
    quantity       int not null,
    status         varchar(100) not null,
    price          double not null,
    imgLink        varchar(512),
    uploadTime     varchar(512) not null
);
create table robots
(
    name          varchar(512) not null,
    gardenId      int not null references gardens(id),
    description   varchar(1024),
    batteryLevel  int not null,
    primary key (name, gardenId)
);
create table likedPosts
(
    postId      int not null references forumPosts(id),
    gardenId    int not null references gardens(id),
    primary     key (postId, gardenId)
);
create table comments
(
    id          int not null auto_increment constraint pk_comments primary key,
    postId      int not null references forumPosts(id),
    gardenId    int not null references gardens(id),
    text        varchar(512) not null
);
create table likedComments
(
    commentId      int not null references comments(id),
    gardenId       int not null references gardens(id),
    primary        key (commentId, gardenId)
);

