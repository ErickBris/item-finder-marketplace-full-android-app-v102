-- phpMyAdmin SQL Dump
-- version 3.5.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Sep 03, 2015 at 10:10 PM
-- Server version: 5.5.29
-- PHP Version: 5.4.10

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `db_itemfinder`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_itemfinder_authentication`
--

CREATE TABLE `tbl_itemfinder_authentication` (
  `authentication_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `role_id` int(11) NOT NULL,
  `is_deleted` int(11) NOT NULL,
  `deny_access` int(11) NOT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `tbl_itemfinder_authentication`
--

INSERT INTO `tbl_itemfinder_authentication` (`authentication_id`, `username`, `password`, `name`, `role_id`, `is_deleted`, `deny_access`) VALUES
(1, 'admin', '3dba44de6dbf6ad13444799ed798f5b8', 'Admin', 1, 0, 0),
(2, 'guest', '25d55ad283aa400af464c76d713c07ad', 'Guest', 2, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_itemfinder_categories`
--

CREATE TABLE `tbl_itemfinder_categories` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(100) NOT NULL,
  `created_at` int(11) NOT NULL,
  `updated_at` int(11) NOT NULL,
  `is_deleted` int(11) NOT NULL,
  `pid` int(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=15 ;

--
-- Dumping data for table `tbl_itemfinder_categories`
--

INSERT INTO `tbl_itemfinder_categories` (`category_id`, `category`, `created_at`, `updated_at`, `is_deleted`, `pid`) VALUES
(1, 'Books', 1403798263, 1403798263, 0, 0),
(2, 'Cellphones', 1403798263, 1403798263, 0, 0),
(3, 'Romance', 1403798263, 1405495448, 0, 1),
(4, 'Android', 1403798263, 1403798263, 0, 2),
(5, 'Adventure', 1405495383, 1405495383, 0, 1),
(6, 'Apple', 1405594181, 1405594181, 0, 2),
(7, 'Garments', 1405595834, 1405595834, 0, 0),
(8, 'Long Sleeve', 1405595846, 1405595846, 0, 7),
(9, 'Long Pants', 1405595859, 1405595859, 0, 7),
(10, 'Short Pants', 1405595872, 1405595872, 0, 7),
(11, 'Sony', 1405626500, 1405626500, 0, 2),
(12, 'No Pants', 1410626950, 1410626950, 0, 10),
(13, 'With Pants', 1410627158, 1410627158, 0, 10),
(14, 'Windows Phone', 1441310720, 1441310741, 0, 2);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_itemfinder_items`
--

CREATE TABLE `tbl_itemfinder_items` (
  `item_id` int(11) NOT NULL AUTO_INCREMENT,
  `item_name` varchar(255) NOT NULL,
  `item_desc` text NOT NULL,
  `item_price` varchar(20) NOT NULL,
  `category_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `created_at` int(11) NOT NULL,
  `updated_at` int(11) NOT NULL,
  `featured` int(11) NOT NULL,
  `is_deleted` int(11) NOT NULL,
  `item_status` int(11) NOT NULL DEFAULT '-1',
  `item_type` int(11) NOT NULL DEFAULT '-1',
  `is_sold` int(11) NOT NULL DEFAULT '0',
  `lat` varchar(40) NOT NULL DEFAULT '0',
  `lon` varchar(40) NOT NULL DEFAULT '0',
  `is_published` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`item_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `tbl_itemfinder_items`
--

INSERT INTO `tbl_itemfinder_items` (`item_id`, `item_name`, `item_desc`, `item_price`, `category_id`, `user_id`, `created_at`, `updated_at`, `featured`, `is_deleted`, `item_status`, `item_type`, `is_sold`, `lat`, `lon`, `is_published`) VALUES
(1, 'iPhone 5s - Black', 'Brand new iPhone 5s&#10;&#10;Click our email and we discussed it over there.&#10;&#10;&#10;&#10;&#10;&#10;Or you can call or text us. :)', '$699', 6, 1, 1405594272, 1405594272, 0, 0, 0, 0, 0, '42.7615326803831', '-72.9889286341697', 1),
(2, 'Samsung Galaxy S5', 'Samsung Galaxy S5 at very low price. Unlock 32GB at a reasonable price. You wont worry buying this product. This the best deal ever. Email or Contact me if interested.&#10;&#10;asasasas', '$599', 4, 1, 1441225842, 1441225842, 1, 0, 0, 0, 0, '42.7617326803831', '-72.9889786341697', 1),
(3, 'Sony Xperia Z1', 'Brand new Sony Xperia Z1 Unlocked, No Defects, Working 100%, Open Box to check if the Item is Functioning well. Email or Contact us for this. Thanks.Brand new Sony Xperia Z1 Unlocked, No Defects, Working 100%, Open Box to check if the Item is Functioning well. Email or Contact us for this. Thanks.Brand new Sony Xperia Z1 Unlocked, No Defects, Working 100%, Open Box to check if the Item is Functioning well. Email or Contact us for this. Thanks.&#10;&#10;Brand new Sony Xperia Z1 Unlocked, No Defects, Working 100%, Open Box to check if the Item is Functioning well. Email or Contact us for this. Thanks.', '$740', 4, 1, 1441228015, 1441228015, 0, 0, 0, 0, 0, '42.7695326803831', '-72.9891286341697', 1),
(4, 'Sony Xperia Z5', 'It looks like Sony will be joining the ranks of Samsung, Apple, and LG in 2016, at least it''s looking that way. The Sony Xperia Z5 Ultra is rumored to be a 6&quot; premium Phablet that is expected to rival the 2016 releases of the Galaxy Note 6, LG G5 Note, and the iPhone 7 Plus. 2016 is shaping up to be the year of the Phablet, and there will be many premium Phablets to choose from.The release of the Xperia Z5 is a foregone conclusion, but the new Sony Xperia Z5 Ultra rumors are not.&#10;&#10;&#10;&#10;The new Sony Phablet is expected to be appeal to Sony enthusiasts while giving you yet another Z5 option to choose from.The Xperia Z5 Ultra is said to be released alongside the Xperia Compact, and the standard 5.5&quot; Z5. &quot;441&quot;', '$999', 4, 1, 1441310658, 1441310658, 0, 0, 0, 1, 0, '42.7915326803831', '-72.9981286341697', 1),
(5, 'iPhone 6+', 'In very good condition.', '$1299', 6, 2, 1441310801, 1441310801, 1, 0, 0, 0, 0, '42.7115326803831', '-72.9811286341697', 1);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_itemfinder_news`
--

CREATE TABLE `tbl_itemfinder_news` (
  `news_id` int(11) NOT NULL AUTO_INCREMENT,
  `news_content` text NOT NULL,
  `news_title` varchar(100) NOT NULL,
  `news_url` varchar(100) NOT NULL,
  `photo_url` varchar(200) NOT NULL,
  `created_at` int(11) NOT NULL,
  `updated_at` int(11) NOT NULL,
  `is_deleted` int(11) NOT NULL,
  PRIMARY KEY (`news_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `tbl_itemfinder_news`
--

INSERT INTO `tbl_itemfinder_news` (`news_id`, `news_content`, `news_title`, `news_url`, `photo_url`, `created_at`, `updated_at`, `is_deleted`) VALUES
(1, 'Apparently, there’s still life in the old social video dog yet. FightMe, an iOS app that lets you challenge others by recording and sharing 30 second-long videos, has raised a further, modest seed round of funding.\\nNew backing comes from VC firm HTG Ventures, Daniel and Raphael Khalili (who also invested in Yahoo-acquired Summly), and David Reuben Junior of Reuben Brothers, who, together, have put $1.35 million into the London-based started. This adds to the $500,000 FightMe raised back in October 2013.\\nDescribed as a “social video network” designed to showcase any talent or opinion, the iOS app (with Android pegged to follow) lets you post your own 30-second, unedited videos, and add appropriate hashtags so that others can browse and discover your videos. They can then choose to respond with a similar themed video, as well as follow you, or share your video.\r\n', 'FightMe, An App That Lets You Challenge Others Through Video, Picks Up $1.35M In New Backing', 'http://techcrunch.com/2014/06/12/fightme-again/', 'http://tctechcrunch2011.files.wordpress.com/2014/06/screen-shot-fightme-singing.png?w=738', 1402559958, 1402559958, 0),
(2, 'There have been a lot of murmurs that Amazon would turn on a new music streaming service this week, and it looks like that’s just what it quietly did a little while ago. A link to Amazon Prime Music, if you are an Amazon Prime subscriber that is, now takes you to a page heavy on playlists, promising over 1 million songs ready for your streaming pleasure, on top of the downloading service that Amazon already offered. And Amazon has also relaunched its mobile app on iOS with a new name: it’s now called Amazon Music instead of Amazon Cloud Player, which now also features access to Amazon Prime Music.\\nBut, unless Amazon is going to add more features before an official unveiling, I’m not sure what we’re seeing right now is quite a Spotify killer. The service as it is right now appears to be missing the most current releases; and you have to go through several steps, including adding music to your library, before you can actually stream tracks. On a search results page, what you still get is the option to listen to an excerpt of a track.', 'Amazon Turns On Prime Music Streaming, Sans Current Hits', 'http://techcrunch.com/2014/06/11/amazon-turns-on-prime-music-streaming-sans-current-hits/', 'http://tctechcrunch2011.files.wordpress.com/2014/06/screen-shot-2014-06-12-at-07-20-14.png?w=738', 1405628252, 1405628252, 0);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_itemfinder_photos`
--

CREATE TABLE `tbl_itemfinder_photos` (
  `photo_id` int(11) NOT NULL AUTO_INCREMENT,
  `photo_url` varchar(255) NOT NULL,
  `thumb_url` varchar(255) NOT NULL,
  `item_id` int(11) NOT NULL,
  `created_at` int(11) NOT NULL,
  `updated_at` int(11) NOT NULL,
  `is_deleted` int(11) NOT NULL,
  PRIMARY KEY (`photo_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=8 ;

--
-- Dumping data for table `tbl_itemfinder_photos`
--

INSERT INTO `tbl_itemfinder_photos` (`photo_id`, `photo_url`, `thumb_url`, `item_id`, `created_at`, `updated_at`, `is_deleted`) VALUES
(1, 'http://mangasaurgames.com/apps/itemfinder-v1.0/upload_pic/photo_53c7aaafb933c.png', 'http://mangasaurgames.com/apps/itemfinder-v1.0/upload_pic/photo_53c7aaafb933c.png', 1, 1405594287, 1405594287, 0),
(2, 'http://mangasaurgames.com/apps/itemfinder-v1.0/upload_pic/photo_53c7aaafb9cc8.png', 'http://mangasaurgames.com/apps/itemfinder-v1.0/upload_pic/photo_53c7aaafb9cc8.png', 1, 1405594287, 1405594287, 0),
(3, 'http://mangasaurgames.com/apps/itemfinder-v1.0/upload_pic/photo_53c7ac19d080d.png', 'http://mangasaurgames.com/apps/itemfinder-v1.0/upload_pic/photo_53c7ac19d080d.png', 2, 1405594299, 0, 0),
(4, 'http://mangasaurgames.com/apps/itemfinder-v1.0/upload_pic/photo_53c7ac1ab9ab8.png', 'http://mangasaurgames.com/apps/itemfinder-v1.0/upload_pic/photo_53c7ac1ab9ab8.png', 2, 1405594299, 0, 0),
(5, 'http://mangasaurgames.com/apps/itemfinder-v1.0/upload_pic/photo_53c828efe9c35.png', 'http://mangasaurgames.com/apps/itemfinder-v1.0/upload_pic/photo_53c828efe9c35.png', 3, 1405626607, 1405626607, 0),
(6, 'http://smartphone2016.com/wp-content/uploads/2015/03/Samsung-Galaxy-S7-LG-G5-and-Sony-Xperia-Z5-Release-Date-update.jpg', 'http://smartphone2016.com/wp-content/uploads/2015/03/Samsung-Galaxy-S7-LG-G5-and-Sony-Xperia-Z5-Release-Date-update.jpg', 4, 1441049042, 0, 0),
(7, 'http://cnet1.cbsistatic.com/hub/i/r/2014/09/15/98b70517-3a22-4e39-b48b-9bb396a23407/thumbnail/770x433/b75c788b1c8f96233658bac84d2d10d9/plutus91503.jpg', 'http://cnet1.cbsistatic.com/hub/i/r/2014/09/15/98b70517-3a22-4e39-b48b-9bb396a23407/thumbnail/770x433/b75c788b1c8f96233658bac84d2d10d9/plutus91503.jpg', 5, 1441049042, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_itemfinder_reviews`
--

CREATE TABLE `tbl_itemfinder_reviews` (
  `review_id` int(11) NOT NULL AUTO_INCREMENT,
  `review` varchar(140) NOT NULL,
  `parent_user_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `created_at` int(11) NOT NULL,
  `updated_at` int(11) NOT NULL,
  `is_deleted` int(11) NOT NULL,
  PRIMARY KEY (`review_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=8 ;

--
-- Dumping data for table `tbl_itemfinder_reviews`
--

INSERT INTO `tbl_itemfinder_reviews` (`review_id`, `review`, `parent_user_id`, `user_id`, `created_at`, `updated_at`, `is_deleted`) VALUES
(1, 'This Seller is Cool.', 2, 1, 1405628325, 1405628325, 0),
(2, 'Good', 1, 1, 1405641873, 1405641873, 0),
(3, 'Very nice and patient. Loving this seller. Thumbs up.', 1, 2, 1441088284, 1441088284, 0),
(4, 'Trusted and recommended seller. Great after sales service.', 1, 2, 1441088709, 1441088709, 0),
(5, 'Amazing product. Would love to buy more on this seller.', 1, 1, 1441088714, 1441088714, 0),
(6, 'I love this seller.', 2, 1, 1441213543, 1441213543, 0),
(7, 'Really satisfied with the quality and speed of delivery. Mats look great and have already recommended to family and friends.\n', 2, 1, 1441213703, 1441213703, 0);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_itemfinder_users`
--

CREATE TABLE `tbl_itemfinder_users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `full_name` varchar(100) NOT NULL,
  `username` varchar(40) NOT NULL,
  `password` varchar(40) NOT NULL,
  `login_hash` varchar(200) NOT NULL,
  `facebook_id` text NOT NULL,
  `twitter_id` text NOT NULL,
  `deny_access` int(11) NOT NULL,
  `thumb_url` varchar(100) NOT NULL,
  `photo_url` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `sms_no` varchar(100) NOT NULL,
  `phone_no` varchar(100) NOT NULL,
  `created_at` int(11) NOT NULL,
  `updated_at` int(11) NOT NULL,
  `last_logged` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `tbl_itemfinder_users`
--

INSERT INTO `tbl_itemfinder_users` (`user_id`, `full_name`, `username`, `password`, `login_hash`, `facebook_id`, `twitter_id`, `deny_access`, `thumb_url`, `photo_url`, `email`, `sms_no`, `phone_no`, `created_at`, `updated_at`, `last_logged`) VALUES
(1, 'John Doe', 'dummy_user', '25d55ad283aa400af464c76d713c07ad', 'FJMG0Cp6XLTNFyMUcrodmbBmW4xhNTM5MmU0NDQw', '', '', 0, 'http://192.168.254.107/personal/itemfinder-paginator/upload_pic/thumb_55e6ff3a3f61d.jpg', 'http://192.168.254.107/personal/itemfinder-paginator/upload_pic/photo_55e6ff3a3f6b4.jpg', 'dummy_user@gmail.com', '+123 456 7890', '+123 456 7890', 1403798350, 1403798350, 1441278093),
(2, 'Cpt Morgan', '', '', '+Ga+yomcdEWV5yBfGdfHSMoP75o4NjZkZWE1YjMz', '261528977363938', '', 0, 'http://mangasaurgames.com/apps/itemfinder-v1.0/upload_pic/thumb_53c86b7ac3f1f.png', 'http://mangasaurgames.com/apps/itemfinder-v1.0/upload_pic/photo_53c86b7ac8dc9.png', 'mg.user001@gmail.com', '+1234455888', '+1235588233', 1405643524, 0, 1405643524);
