<?php

  require_once '../header_rest.php';

  $controllerUser = new ControllerUser();

  if( !empty($_POST['username']) )
      $username = $_POST['username'];

  if( !empty($_POST['password']) )
      $password = md5($_POST['password']);

  $full_name = "";
  if( !empty($_POST['full_name']) )
      $full_name = $_POST['full_name'];

  $email = "";
  if( !empty($_POST['email']) )
      $email = $_POST['email'];

  if( !empty($_POST['facebook_id']) )
      $facebook_id = $_POST['facebook_id'];

  if( !empty($_POST['twitter_id']) )
      $twitter_id = $_POST['twitter_id'];

  $phone_no = "";
  if( !empty($_POST['phone_no']) )
      $phone_no = $_POST['phone_no'];

  $sms_no = "";
  if( !empty($_POST['sms_no']) )
      $sms_no = $_POST['sms_no'];

  if( !empty($username) && !empty($password) && !empty($full_name) && !empty($email) ) {
      
      if(!$controllerUser->isUserExist($username)) {

            if($controllerUser->isEmailExist($email)) {

                $json = "{
                            \"status\" : {
                                          \"status_code\" : \"1\",
                                          \"status_text\" : \"Email already registered.\"
                                        }
                        }";
            }
            else {

                $itm = new User();
                $itm->username = $username;
                $itm->password = $password;
                $itm->full_name = $full_name;
                $itm->email = $email;
                $itm->facebook_id = '';
                $itm->twitter_id = '';
                $itm->created_at = time();
                $itm->updated_at = time();
                $itm->last_logged = time();
                $itm->phone_no = $phone_no;
                $itm->sms_no = $sms_no;

                $controllerUser->registerUser($itm);
                $user = $controllerUser->loginUser($username, $itm->password);
                if($user != null) {
                    // update the hash
                    $controllerUser->updateUserHash($user);
                    $json = translateJSON($user);
                }
                else {

                    $json = "{
                                \"status\" : {
                                              \"status_code\" : \"1\",
                                              \"status_text\" : \"Username/Password Invalid.\"
                                            }
                            }";
                }

            }
      }

      else {
          $json = "{
                        \"status\" : {
                                      \"status_code\" : \"4\",
                                      \"status_text\" : \"Username Exist.\"
                                    }
                    }";
      }


      

      echo $json;
  }

  else if( !empty($facebook_id) ) {

      if(!$controllerUser->isFacebookIdExist($facebook_id)) {
            $itm = new User();
            $itm->username = '';
            $itm->password = '';
            $itm->full_name = $full_name;
            $itm->email = $email;
            $itm->facebook_id = $facebook_id;
            $itm->twitter_id = '';
            $itm->created_at = time();
            $itm->updated_at = time();
            $itm->last_logged = time();
            $itm->phone_no = '';
            $itm->sms_no = '';
            
            $user = $controllerUser->loginFacebook($facebook_id);

            if($user == null)
              $controllerUser->registerUser($itm);

            $user = $controllerUser->loginFacebook($facebook_id);
            if($user != null) {
                // update the hash
                $controllerUser->updateUserHash($user);
                $json = translateJSON($user);
            }
      }
      else {

            $user = $controllerUser->loginFacebook($facebook_id);

            if($user != null) {
                // update the hash
                $controllerUser->updateUserHash($user);
                $json = translateJSON($user);
            }
            else {

                $json = "{
                            \"status\" : {
                                          \"status_code\" : \"1\",
                                          \"status_text\" : \"Username/Password Invalid.\"
                                        }
                        }";

            }
      }

      echo $json;
  }

  else if( !empty($twitter_id) ) {

      if(!$controllerUser->isTwitterIdExist($twitter_id)) {
            $itm = new User();
            $itm->username = '';
            $itm->password = '';
            $itm->full_name = $full_name;
            $itm->email = $email;
            $itm->facebook_id = '';
            $itm->twitter_id = $twitter_id;
            $itm->created_at = time();
            $itm->updated_at = time();
            $itm->last_logged = time();
            $itm->phone_no = '';
            $itm->sms_no = '';

            $controllerUser->registerUser($itm);
            $user = $controllerUser->loginTwitter($twitter_id);

            if($user != null) {
                // update the hash
                $controllerUser->updateUserHash($user);
                $json = translateJSON($user);
            }
            else {

                $json = "{
                            \"status\" : {
                                          \"status_code\" : \"1\",
                                          \"status_text\" : \"Username/Password Invalid.\"
                                        }
                        }";

            }
      }
      else {

            $user = $controllerUser->loginTwitter($twitter_id);

            if($user != null) {
                // update the hash
                $controllerUser->updateUserHash($user);
                $json = translateJSON($user);
            }
            else {

                $json = "{
                            \"status\" : {
                                          \"status_code\" : \"1\",
                                          \"status_text\" : \"111Username/Password Invalid.\"
                                        }
                        }";

            }
      }

      echo $json;
  }
  else {

      $json = "{
                \"status\" : {
                              \"status_code\" : \"3\",
                              \"status_text\" : \"Invalid Access.\"
                            }
                }";

      echo $json;
  }



  function translateJSON($itm) {

      $json = "{
                      \"user_info\" : {
                                    \"user_id\" : \"$itm->user_id\",
                                    \"username\" : \"$itm->username\",
                                    \"login_hash\" : \"$itm->login_hash\",
                                    \"facebook_id\" : \"$itm->facebook_id\",
                                    \"twitter_id\" : \"$itm->twitter_id\",
                                    \"full_name\" : \"$itm->full_name\",
                                    \"thumb_url\" : \"$itm->thumb_url\",
                                    \"photo_url\" : \"$itm->photo_url\",
                                    \"email\" : \"$itm->email\",
                                    \"created_at\" : \"$itm->created_at\",
                                    \"updated_at\" : \"$itm->updated_at\",
                                    \"last_logged\" : \"$itm->last_logged\",
                                    \"phone_no\" : \"$itm->phone_no\",
                                    \"sms_no\" : \"$itm->sms_no\"
                                    },

                      \"status\" : {
                                    \"status_code\" : \"-1\",
                                    \"status_text\" : \"Success.\"
                                  }
                  }";


      return $json;
  }

?>