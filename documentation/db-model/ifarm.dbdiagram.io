 Table Gardens {
   id int [pk]
   tier enum
 }
 
 Table Plants {
   id int [pk]
   name string 
   imgLink string
 }
 
 Table OwnedPlants {
   id int [pk]
   plantId int [ref: > Plants.id]
   gardenId int [ref: > Gardens.id]
   status string
 }

 Table ForumPosts {
   id int [pk]
   gardenId int [ref: > Gardens.id]
   text string
   string imgLink
   uploadTime datetime
 }
 
 Table Scans {
   ownedPlantId int [pk, ref: > OwnedPlants.id]
   scanTime datetime [pk]
   fertilizerLevel int
   waterLevel int
   expectedYield int 
   health int 
   growth int 
 }
 
 Table MarketplaceAds {
   id int [pk]
   gardenId int [ref: > Gardens.id]
   plantId int [ref: > Plants.id]
   description string
   quantity int
   status string
   price double
   uploadTime datetime
   imgLink string
 }
 
 Table Robots {
   name string [pk]
   gardenId int [pk, ref: > Gardens.id]
   description string
   batteryLevel int
 }
 
 Table LikedPosts {
   postId int [pk, ref: > ForumPosts.id]
   gardenId int [pk, ref: > Gardens.id]
 }
 
 Table Comments {
   id int [pk]
   postId int [ref: > ForumPosts.id]
   gardenId int [ref: > Gardens.id]
   text string 
 }
 
 Table LikedComments {
   commentId int [pk, ref: > Comments.id]
   gardenId int [pk, ref: > Gardens.id]
 }
 