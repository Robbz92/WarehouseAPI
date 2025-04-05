-- Creating table `article`
CREATE TABLE IF NOT EXISTS `article` (
     `id` int(11) NOT NULL AUTO_INCREMENT,
     `name` varchar(100) NOT NULL,
     `available_stock` int(11) NOT NULL,
     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Creating table `product`
CREATE TABLE IF NOT EXISTS `product` (
     `id` int(11) NOT NULL AUTO_INCREMENT,
     `name` varchar(100) NOT NULL,
     `price` decimal(10,0) NOT NULL,
     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Creating table `product_article`
CREATE TABLE IF NOT EXISTS `product_article` (
    `product_id` int(11) NOT NULL,
    `article_id` int(11) NOT NULL,
    `quantity` int(11) NOT NULL,
    PRIMARY KEY (`product_id`,`article_id`),
    CONSTRAINT `product_article_articles_FK` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`),
    CONSTRAINT `product_article_products_FK` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
