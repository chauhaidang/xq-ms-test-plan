CREATE TABLE IF NOT EXISTS`requirements`(
    `req_id` int AUTO_INCREMENT PRIMARY KEY,
    `uuid` varchar(40) NOT NULL,
    `title` varchar(100) NOT NULL,
    `description` varchar(500) NOT NULL,
    `scopes` varchar(500) NOT NULL,
    `tags` varchar(200) NOT NULL,
    `references` varchar(500) NOT NULL,
    `created_at` date NOT NULL,
    `created_by` varchar(20) NOT NULL,
    `updated_at` date DEFAULT NULL,
    `updated_by` varchar(20) DEFAULT NULL
    );

CREATE TABLE IF NOT EXISTS`linked_requirements`(
    `link_id` int AUTO_INCREMENT PRIMARY KEY,
    `reqa_id` int NOT NULL,
    `reqb_id` int NOT NULL,
    `created_at` date NOT NULL,
    `created_by` varchar(20) NOT NULL,
    `updated_at` date DEFAULT NULL,
    `updated_by` varchar(20) DEFAULT NULL
    );