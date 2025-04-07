-- Table structure for table `article`
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `name` varchar(255) NOT NULL,
   `available_stock` int(11) NOT NULL,
   `article_id` int(11) NOT NULL,
   PRIMARY KEY (`id`),
   UNIQUE KEY `article_id` (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Table structure for table `product`
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `name` varchar(100) NOT NULL,
   `price` decimal(10,0) NOT NULL,
   PRIMARY KEY (`id`),
   UNIQUE KEY `product_unique` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Table structure for table `product_article`
DROP TABLE IF EXISTS `product_article`;
CREATE TABLE `product_article` (
   `product_id` int(11) NOT NULL,
   `article_id` int(11) NOT NULL,
   `quantity` int(11) NOT NULL,
   PRIMARY KEY (`product_id`,`article_id`),
   KEY `product_article_articles_FK` (`article_id`),
   CONSTRAINT `product_article_articles_FK` FOREIGN KEY (`article_id`) REFERENCES `article` (`article_id`),
   CONSTRAINT `product_article_products_FK` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
