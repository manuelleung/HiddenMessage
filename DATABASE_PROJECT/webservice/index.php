<?php
/**
 PHP API for Login, Register, Change password, Reset password, New Messages
 **/
if (isset($_POST['tag']) && $_POST['tag'] != '') {
    // Get tag (REQUEST)
    $tag = $_POST['tag'];
 
    // Include Database handler (connect to db)
    require_once 'include/DB_Functions.php';
    $db = new DB_Functions();
    // response Array
    $response = array("tag" => $tag, "success" => 0, "error" => 0);
 
    // check for tag type
    if ($tag == 'login') {
            // Request type is check Login
            $email = mysql_real_escape_string($_POST['email']);
            $password = mysql_real_escape_string($_POST['password']);

            // check for user
            $user = $db->getUserByEmailAndPassword($email, $password);
            if ($user != false) {
                // user found
                // echo json with success = 1
                $response["success"] = 1;
                $response["user"]["fname"] = $user["firstname"];
                $response["user"]["lname"] = $user["lastname"];
                $response["user"]["email"] = $user["email"];
                $response["user"]["uname"] = $user["username"];
                $response["user"]["uid"] = $user["unique_id"];
                $response["user"]["created_at"] = $user["created_at"];

                echo json_encode($response);
            } 
            else {
                // user not found
                // echo json with error = 1
                $response["error"] = 1;
                $response["error_msg"] = "Incorrect email or password!";
                echo json_encode($response);
            }
    }
    else if ($tag == 'changepass') {
        $email = mysql_real_escape_string($_POST['email']);

        $newpassword = mysql_real_escape_string($_POST['newpas']);

        $hash = $db->hashSSHA($newpassword);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"];
        $subject = "Change Password Notification";
        $message = "Hello user, your Password has been sucessfully changed.";
        $from = "contact@gmail.com";
        $headers = "From:" . $from;
        if ($db->isUserExisted($email)) {
            $user = $db->forgotPassword($email, $encrypted_password, $salt);
            if ($user) {
                $response["success"] = 1;
                mail($email,$subject,$message,$headers);
                echo json_encode($response);
            }
            else {
                $response["error"] = 1;
                echo json_encode($response);
            }
 
        }
        else {
            $response["error"] = 2;
            $response["error_msg"] = "User not exist";
            echo json_encode($response);
        }
    }
    else if ($tag == 'forgotpass'){
        $email = mysql_real_escape_string($_POST['email']);
        $randomcode = $db->random_string();
        $hash = $db->hashSSHA($randomcode);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"];
        $subject = "Password Recovery";
        $message = "Hello user, Your temporary Password is $randomcode . Please Login and change as soon as possible <3.";
        $from = "hiddenmessage.windcore@gmail.com";
        $headers = "From:" . $from;
        if ($db->isUserExisted($email)) {
            $user = $db->forgotPassword($email, $encrypted_password, $salt);
            if ($user) {
                $response["success"] = 1;
                mail($email,$subject,$message,$headers);
                echo json_encode($response);
            }
            else {
                $response["error"] = 1;
                echo json_encode($response);
            }
            // user is already existed - error response 
        }
        else {
            $response["error"] = 2;
            $response["error_msg"] = "User not exist";
            echo json_encode($response);
        }
    } //message
    else if($tag == 'newmessage') {
        //request type new message
        $name = mysql_real_escape_string($_POST['name']);
        $title = mysql_real_escape_string($_POST['title']); //title
        $content = mysql_real_escape_string($_POST['content']); //message content
        $latitude = mysql_real_escape_string($_POST['latitude']);
        $longitude = mysql_real_escape_string($_POST['longitude']);

        $message = $db->storeMessage($name, $title, $content, $latitude, $longitude);
        if($message) {
            // message stored successfully
            $response["success"] = 1;
            $response["message"]["name"] = $message["name"];
            $response["message"]["title"] = $message["title"];
            $response["message"]["content"] = $message["content"];
            $response["message"]["latitude"] = $message["latitude"];
            $response["message"]["longitude"] = $message["longitude"];
            $response["message"]["created_at"] = $message["created_at"];
            
            echo json_encode($response);
        } else {
            //failed to store message
            $response["error"] = 1;
            $response["error_msg"] = "JSON Error occured in New Message";
            echo json_encode($response);
        }
    }
    else if ($tag == 'allmessages') {
        $result = $db->getAllMessages();
        $no_of_rows = mysql_num_rows($result);
        
        if($no_of_rows>0) {
            $response["success"] = 1;
            $count=0;
            while($row = mysql_fetch_array($result) ) {
                $response[$count]["name"] = $row["name"];
                $response[$count]["title"] = $row["title"];
                $response[$count]["content"] = $row["content"];
                $response[$count]["latitude"] = $row["latitude"];
                $response[$count]["longitude"] = $row["longitude"];
                $count = $count+1;
            }
            echo json_encode($response);

        } else {
            
            $response["error"] = 1;
            $response["error_msg"] = "JSON Error occured in allmessages";
            echo json_encode($response);
        }
    }
    else if ($tag == 'register') {
        // Request type is Register new user
        $fname = mysql_real_escape_string($_POST['fname']);
        $lname = mysql_real_escape_string($_POST['lname']);
        $email = mysql_real_escape_string($_POST['email']);
        $uname = mysql_real_escape_string($_POST['uname']);
        $password = mysql_real_escape_string($_POST['password']);
        $subject = "Registration";
        $message = "Hello $fname, You have sucessfully registered to Hidden Message.";
        $from = "hiddenmessage.windcore@gmail.com";
        $headers = "From:" . $from;
        // check if user is already existed
        if ($db->isUserExisted($email)) {
            // user is already existed - error response
            $response["error"] = 2;
            $response["error_msg"] = "User already existed";
            echo json_encode($response);
        }
        else if(!$db->validEmail($email)){
            //invalid email
            $response["error"] = 3;
            $response["error_msg"] = "Invalid Email Id";
            echo json_encode($response);
        }
        else {
            // store user
            $user = $db->storeUser($fname, $lname, $email, $uname, $password);
            if ($user) {
                // user stored successfully
                $response["success"] = 1;
                $response["user"]["fname"] = $user["firstname"];
                $response["user"]["lname"] = $user["lastname"];
                $response["user"]["email"] = $user["email"];
                $response["user"]["uname"] = $user["username"];
                $response["user"]["uid"] = $user["unique_id"];
                $response["user"]["created_at"] = $user["created_at"];
                mail($email,$subject,$message,$headers);
                echo json_encode($response);
            } else {
                // user failed to store
                $response["success"] = 0;
                $response["error"] = 1;
                $response["error_msg"] = "JSON Error occured in Registration";
                echo json_encode($response);
            }
        }
    } else {
         $response["error"] = 3;
         $response["error_msg"] = "JSON ERROR";
        echo json_encode($response);
    }
} else {
    //print phpinfo();
    echo "hiddendb";
}
?>