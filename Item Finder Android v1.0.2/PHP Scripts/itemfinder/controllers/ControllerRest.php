<?php

 
class ControllerRest
{
 
    private $db;
    private $pdo;
    function __construct() 
    {
        // connecting to database
        $this->db = new DB_Connect();
        $this->pdo = $this->db->connect();
    }
 
    function __destruct() { }
 
    public function getResultPhotos() 
    {
        $stmt = $this->pdo->prepare('SELECT * FROM tbl_storefinder_photos WHERE is_deleted = 0');

        $stmt->execute();
        return $stmt;
    }

    public function getResultStores() 
    {
        $stmt = $this->pdo->prepare('SELECT tbl_storefinder_stores.store_id,
                                            tbl_storefinder_stores.store_name,
                                            tbl_storefinder_stores.store_address,
                                            tbl_storefinder_stores.store_desc,
                                            tbl_storefinder_stores.lat,
                                            tbl_storefinder_stores.lon,
                                            tbl_storefinder_stores.sms_no,
                                            tbl_storefinder_stores.phone_no,
                                            tbl_storefinder_stores.email,
                                            tbl_storefinder_stores.website,
                                            tbl_storefinder_stores.category_id,
                                            tbl_storefinder_stores.created_at,
                                            tbl_storefinder_stores.updated_at,
                                            tbl_storefinder_stores.featured,
                                            tbl_storefinder_stores.is_deleted,
                                            COALESCE(SUM(tbl_storefinder_ratings.rating), 0) as rating_total, 
                                            COALESCE(COUNT(tbl_storefinder_ratings.rating), 0) as rating_count
                                            
                                    FROM tbl_storefinder_stores 
                                    LEFT OUTER JOIN tbl_storefinder_ratings 
                                    ON tbl_storefinder_stores.store_id = tbl_storefinder_ratings.store_id 
                                    WHERE is_deleted = 0 GROUP BY tbl_storefinder_stores.store_id');
        $stmt->execute();
        return $stmt;
    }

    
    public function getResultNews() 
    {
        $stmt = $this->pdo->prepare('SELECT * FROM tbl_itemfinder_news WHERE is_deleted = 0');
        $stmt->execute();
        return $stmt;
    }

    public function getResultReviews($tmpFrom, $tmpTo, $store_id) 
    {
        $stmt = $this->pdo->prepare('SELECT review_id, review, tbl_storefinder_reviews.created_at, thumb_url, photo_url, username, full_name 
                                        FROM tbl_storefinder_reviews 
                                        INNER JOIN tbl_storefinder_users ON 
                                                        tbl_storefinder_reviews.user_id = tbl_storefinder_users.user_id 

                                        WHERE is_deleted = 0 AND created_at >= :tmpFrom AND created_at <= :tmpTo AND store_id = :store_id');
        
        $stmt->execute(
                        array('tmpFrom' => $tmpFrom,
                                    'tmpTo' => $tmpTo,
                                    'store_id' => $store_id) );
        return $stmt;
    }

    public function getResultStoresRating($store_id) 
    {
        $stmt = $this->pdo->prepare('SELECT *, SUM(rating) as rating_total, COUNT(rating) as rating_count 
                                    FROM tbl_storefinder_stores 
                                    LEFT OUTER JOIN tbl_storefinder_ratings 
                                    ON tbl_storefinder_stores.store_id = tbl_storefinder_ratings.store_id 
                                    WHERE is_deleted = 0 AND tbl_storefinder_stores.store_id = :store_id GROUP BY tbl_storefinder_stores.store_id');
        $stmt->execute(
                        array('store_id' => $store_id) );

        return $stmt;
    }


    public function getResultReviewsCount($count, $store_id) 
    {
        $stmt = $this->pdo->prepare('SELECT review_id, review, tbl_storefinder_reviews.created_at, thumb_url, photo_url, full_name 
                                        FROM tbl_storefinder_reviews 
                                        INNER JOIN tbl_storefinder_users ON 
                                                        tbl_storefinder_reviews.user_id = tbl_storefinder_users.user_id 

                                        WHERE is_deleted = 0 AND store_id = :store_id LIMIT :count');
        
        $stmt->execute(
                        array( 'count' => $count,
                                    'store_id' => $store_id) );
        return $stmt;
    }

    public function getResultReviewsTotalCount($store_id) 
    {
        $stmt = $this->pdo->prepare('SELECT review_id, review, tbl_storefinder_reviews.created_at, thumb_url, photo_url, full_name 
                                        FROM tbl_storefinder_reviews 
                                        INNER JOIN tbl_storefinder_users ON 
                                                        tbl_storefinder_reviews.user_id = tbl_storefinder_users.user_id 

                                        WHERE is_deleted = 0 AND store_id = :store_id');
        
        $stmt->execute(
                        array( 'store_id' => $store_id) );

        $count = $stmt->rowCount();
        return $count;
    }



    // NEW API CALL

    public function getResultItemsWithParams($min_count, $max_count) 
    {
        $stmt = $this->pdo->prepare('SELECT 
                                        category_id, 
                                        created_at, 
                                        featured, 
                                        item_desc, 
                                        item_id, 
                                        item_name, 
                                        item_price, 
                                        updated_at,
                                        user_id,
                                        item_status,
                                        item_type,
                                        lat, 
                                        lon,
                                        is_published    

                                    FROM tbl_itemfinder_items

                                    WHERE is_deleted = 0 ORDER BY updated_at DESC LIMIT :min_count, :max_count');
        
        $stmt->execute(
                        array( 'min_count' => $min_count,
                                    'max_count' => $max_count) );
        return $stmt;
    }

    public function getResultPhotoItemsWithParams($item_id) 
    {
        $stmt = $this->pdo->prepare('SELECT 
                                        photo_id, 
                                        photo_url, 
                                        thumb_url, 
                                        item_id, 
                                        created_at, 
                                        updated_at, 
                                        is_deleted 

                                    FROM tbl_itemfinder_photos

                                    WHERE is_deleted = 0 AND item_id = :item_id');
        
        $stmt->execute(
                        array( 'item_id' => $item_id) );
        return $stmt;
    }

    public function getResultUserWithParams($user_id)
    {
        $stmt = $this->pdo->prepare('SELECT 
                                        user_id, 
                                        full_name,
                                        username, 
                                        thumb_url, 
                                        photo_url, 
                                        email, 
                                        sms_no, 
                                        phone_no,
                                        created_at,
                                        updated_at,
                                        last_logged 

                                    FROM tbl_itemfinder_users

                                    WHERE user_id = :user_id');
        
        $stmt->execute(
                        array( 'user_id' => $user_id) );
        return $stmt;
    }

    public function getResultCategories() 
    {
        $stmt = $this->pdo->prepare('SELECT * FROM tbl_itemfinder_categories WHERE is_deleted = 0');
        $stmt->execute();
        return $stmt;
    }

    public function getResultItemsByUserId($user_id) 
    {
        $stmt = $this->pdo->prepare('SELECT 
                                        category_id, 
                                        created_at, 
                                        featured, 
                                        item_desc, 
                                        item_id, 
                                        item_name, 
                                        item_price, 
                                        updated_at,
                                        user_id,
                                        item_status,
                                        item_type,
                                        lat, 
                                        lon,
                                        is_published   

                                    FROM tbl_itemfinder_items

                                    WHERE is_deleted = 0 AND user_id = :user_id ORDER BY updated_at');
        
        $stmt->execute(
                        array( 'user_id' => $user_id) );
        return $stmt;
    }

    public function getResultCategoryByCategoryId($category_id) 
    {
        $stmt = $this->pdo->prepare('SELECT * FROM tbl_itemfinder_categories WHERE is_deleted = 0 AND category_id = :category_id');
        $stmt->execute( array( 'category_id' => $category_id) );
        return $stmt;
    }

    public function getResultReviewsByParentStoreId($parent_user_id) 
    {
        $stmt = $this->pdo->prepare('SELECT 

                                            review_id, 
                                            review, 
                                            tbl_itemfinder_reviews.created_at, 
                                            thumb_url, 
                                            photo_url, 
                                            username, 
                                            full_name, 
                                            tbl_itemfinder_reviews.user_id 

                                        FROM 
                                            tbl_itemfinder_reviews 
                                            
                                        INNER JOIN 
                                            tbl_itemfinder_users 

                                        ON tbl_itemfinder_reviews.user_id = tbl_itemfinder_users.user_id 

                                        WHERE is_deleted = 0 AND parent_user_id = :parent_user_id 

                                        ORDER BY tbl_itemfinder_reviews.created_at DESC');
        
        $stmt->execute(
                        array('parent_user_id' => $parent_user_id) );
        return $stmt;
    }

    public function getResultItemsWithCategoryId($category_id) 
    {
        $stmt = $this->pdo->prepare('SELECT 
                                        category_id, 
                                        created_at, 
                                        featured, 
                                        item_desc, 
                                        item_id, 
                                        item_name, 
                                        item_price,  
                                        updated_at,
                                        user_id,
                                        item_status,
                                        item_type,
                                        lat,
                                        lon, 
                                        is_published    

                                    FROM tbl_itemfinder_items

                                    WHERE is_deleted = 0 AND category_id = :category_id ORDER BY updated_at');
        
        $stmt->execute(
                        array( 'category_id' => $category_id) );
        return $stmt;
    }

    public function getResultItemsWithUserId($user_id) 
    {
        $stmt = $this->pdo->prepare('SELECT 
                                        category_id, 
                                        created_at, 
                                        featured, 
                                        item_desc, 
                                        item_id, 
                                        item_name, 
                                        item_price, 
                                        updated_at,
                                        user_id,
                                        item_status,
                                        item_type,
                                        lat,
                                        lon,
                                        is_published   

                                    FROM tbl_itemfinder_items

                                    WHERE is_deleted = 0 AND user_id = :user_id ORDER BY updated_at');
        
        $stmt->execute(
                        array( 'user_id' => $user_id) );
        return $stmt;
    }

    public function getResultAllItems()
    {
        $stmt = $this->pdo->prepare('SELECT 
                                        category_id, 
                                        created_at, 
                                        featured, 
                                        item_desc, 
                                        item_id, 
                                        item_name, 
                                        item_price, 
                                        updated_at,
                                        user_id,
                                        item_status,
                                        item_type,
                                        lat,
                                        lon,
                                        is_published   

                                    FROM tbl_itemfinder_items

                                    WHERE is_deleted = 0 ORDER BY updated_at');
        
        $stmt->execute( );
        return $stmt;
    }

    public function getResultAllFeaturedItems()
    {
        $stmt = $this->pdo->prepare('SELECT 
                                        category_id, 
                                        created_at, 
                                        featured, 
                                        item_desc, 
                                        item_id, 
                                        item_name, 
                                        item_price, 
                                        updated_at,
                                        user_id,
                                        item_status,
                                        item_type,
                                        lat,
                                        lon, 
                                        is_published   

                                    FROM tbl_itemfinder_items

                                    WHERE is_deleted = 0 AND featured = :featured ORDER BY updated_at');
        
        $stmt->execute( array( 'featured' => 1) );
        return $stmt;
    }

    public function getResultItemsWithParamsSearch($keywords, $full_name, $category_id, $sort_by)
    {
        // No Sorting = 0
        // Price = 1
        // Date Posted = 2
        // Name = 3
      

        $sql = "SELECT 
                    tbl_itemfinder_items.category_id, 
                    tbl_itemfinder_items.created_at, 
                    tbl_itemfinder_items.featured, 
                    tbl_itemfinder_items.item_desc, 
                    tbl_itemfinder_items.item_id, 
                    tbl_itemfinder_items.item_name, 
                    tbl_itemfinder_items.item_price, 
                    tbl_itemfinder_items.updated_at,
                    tbl_itemfinder_items.user_id,
                    tbl_itemfinder_items.item_status,
                    tbl_itemfinder_items.item_type,
                    tbl_itemfinder_items.lat,
                    tbl_itemfinder_items.lon,
                    tbl_itemfinder_items.is_published    

                FROM tbl_itemfinder_items 
                INNER JOIN tbl_itemfinder_users ON tbl_itemfinder_items.user_id = tbl_itemfinder_users.user_id 
                WHERE ";

        $arrayParams = array();
        if(!empty($keywords)) {

            $sql .= "(tbl_itemfinder_items.item_name LIKE :item_name OR tbl_itemfinder_items.item_desc LIKE :item_desc) AND ";
            $arrayParams["item_name"] = '%'.$keywords.'%';
            $arrayParams["item_desc"] = '%'.$keywords.'%';
        }

        if(!empty($full_name)) {

            $sql .= "(tbl_itemfinder_users.full_name LIKE :full_name OR tbl_itemfinder_users.username LIKE :username) AND ";
            $arrayParams["full_name"] = '%'.$full_name.'%';
            $arrayParams["username"] = '%'.$full_name.'%';
        }

        if($category_id > 0) {

            $sql .= "tbl_itemfinder_items.category_id = :category_id AND ";
            $arrayParams["category_id"] = $category_id;
        }

        $sql .= "tbl_itemfinder_items.is_deleted = 0 ";

        if($sort_by == 1) {

            $sql .= "ORDER BY tbl_itemfinder_items.item_id ASC ";
        }

        else if($sort_by == 1) {

            $sql .= "ORDER BY tbl_itemfinder_items.item_price ASC ";
        }

        else if($sort_by == 2) {

            $sql .= "ORDER BY tbl_itemfinder_items.created_at DESC ";
        }

        else if($sort_by == 2) {

            $sql .= "ORDER BY tbl_itemfinder_items.item_name ASC ";
        }

        $stmt = $this->pdo->prepare($sql);        
        $stmt->execute( $arrayParams );

        return $stmt;
    }


    public function getResultPhotoItemByPhotoId($photo_id) 
    {
        $stmt = $this->pdo->prepare('SELECT 
                                        photo_id, 
                                        photo_url, 
                                        thumb_url, 
                                        item_id, 
                                        created_at, 
                                        updated_at, 
                                        is_deleted 

                                    FROM tbl_itemfinder_photos

                                    WHERE photo_id = :photo_id');
        
        $stmt->execute(
                        array( 'photo_id' => $photo_id) );
        return $stmt;
    }
}
 
?>