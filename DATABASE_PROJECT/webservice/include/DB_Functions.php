<?php
class DB_Functions {
 
    /*DEPRECATED UNSAFE QUERIES FIX!!!!!!!!!!!!!!! */
    
    private $db;
 
    //put your code here
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }
 
    // destructor
    function __destruct() {
 
    }
 
    /**
     * Random string which is sent by mail to reset password
     */
 
    public function random_string()
    {
        $character_set_array = array();
        $character_set_array[] = array('count' => 7, 'characters' => 'abcdefghijklmnopqrstuvwxyz');
        $character_set_array[] = array('count' => 1, 'characters' => '0123456789');
        $temp_array = array();
        foreach ($character_set_array as $character_set) {
            for ($i = 0; $i < $character_set['count']; $i++) {
                $temp_array[] = $character_set['characters'][rand(0, strlen($character_set['characters']) - 1)];
            }
        }
        shuffle($temp_array);
        return implode('', $temp_array);
    }
 
    public function forgotPassword($email, $newpassword, $salt) {
        $result = mysql_query("UPDATE `users` SET `encrypted_password` = '$newpassword',`salt` = '$salt' WHERE `email` = '$email'");

        if ($result) {
            return true; 
        }
        else {
            echo mysql_error();
            return false;
        }
    }
    
    /**
     * Adding new user to mysql database
     * returns user details
     */
 
    public function storeUser($fname, $lname, $email, $uname, $password) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
        $result = mysql_query("INSERT INTO users(unique_id, firstname, lastname, email, username, encrypted_password, salt, created_at) VALUES('$uuid', '$fname', '$lname', '$email', '$uname', '$encrypted_password', '$salt', NOW())");
        // check for successful store
        if ($result) {
            // get user details
            $user_id = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM users WHERE user_id = $user_id");
            // return user details
            return mysql_fetch_array($result);
        } else {
            echo mysql_error();
            return false;
        }
    }
    
    /**
     * Add new message to mysql database
     * return message details
     */
    public function storeMessage($name, $title, $content, $latitude, $longitude) {
        $result = mysql_query("INSERT INTO messages(name, title, content, latitude, longitude, created_at) VALUES('$name', '$title', '$content', '$latitude', '$longitude', NOW())");
        
        // check for successful store
        if($result) {
            //get message details
            $user_id = mysql_insert_id(); //last inserted id
            $result = mysql_query("SELECT * FROM messages WHERE message_id = $user_id");
            //return details
            return mysql_fetch_array($result);
        } else {
            echo mysql_error();
            return false;
        }
    }
    /**
     * Retrieve all messages
     *
     */
    public function getAllMessages() {
        $result = mysql_query("SELECT message_id, name, title, content, latitude, longitude, created_at FROM messages");
        $no_of_rows = mysql_num_rows($result);
        if($no_of_rows>0) {
            //return mysql_fetch_array($result);
            return $result;
        } else {
            echo "Error in getAllMessages";
            echo mysql_error();
            return false;
        }
    }
 
    /**
     * Verifies user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {
        $result = mysql_query("SELECT * FROM users WHERE email = '$email'") or die(mysql_error());
        // check for result
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            $salt = $result['salt'];
            $encrypted_password = $result['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $result;
            }
        } else {
            echo mysql_error();
            // user not found
            return false;
        }
    }
 
    /**
     * Checks whether the email is valid or fake
     */
    public function validEmail($email)
    {
        $email = filter_var($email, FILTER_SANITIZE_EMAIL);
        if(!filter_var($email, FILTER_VALIDATE_EMAIL)===false) {
            return true;
        }
        else {
            echo mysql_error();
            return false;
        }
    }
 
    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $result = mysql_query("SELECT email from users WHERE email = '$email'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // user existed
            return true;
        } else {
            // user not existed
            echo mysql_error();
            return false;
        }
    }
 
    /**
     * Encrypting password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
    /**
     * Decrypting password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }
 
}
?>