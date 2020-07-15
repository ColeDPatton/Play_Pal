package com.example.p19;

import android.content.Context;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

//import static com.google.common.truth.Truth.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class mocktest1 {



    @Test
    public void saveUserTest(){
        UserProfileActivity UP = Mockito.mock(UserProfileActivity.class);

        User user = new User();

        user.setEmail("test@gmail.com");
        user.setConsole("PC");
        user.setDateofbirth("01/02/1994");
        user.setGamertag("gotshiaz");
        user.setName("workingboy");
        user.setSex("Male");
        user.setPassword("123456");
        user.setUsertype(1);




        Mockito.when(UP.storeUserAsJsonString2(user)).thenReturn(true);
        assertEquals(true, UP.storeUserAsJsonString2(user));
    }

    @Test
    public void loadUserTest(){
        UserProfileActivity UP = Mockito.mock(UserProfileActivity.class);
        MatchedUsersActivity Mt = Mockito.mock(MatchedUsersActivity.class);
        User user = new User();

        user.setEmail("test@gmail.com");
        user.setConsole("PC");
        user.setDateofbirth("01/02/1994");
        user.setGamertag("gotshiaz");
        user.setName("workingboy");
        user.setSex("Male");
        user.setPassword("123456");
        user.setUsertype(1);
        UP.storeUserAsJsonString2(user);




        Mockito.when(Mt.loadCurrentUser2()).thenReturn(true);
        assertEquals(true, Mt.loadCurrentUser2());
    }


    @Test
    public void toJsonTest(){
        User user = Mockito.mock(User.class);


        user.setEmail("test@gmail.com");
        user.setConsole("PC");
        user.setDateofbirth("01/02/1994");
        user.setGamertag("gotshiaz");
        user.setName("workingboy");
        user.setSex("Male");
        user.setPassword("123456");
        user.setUsertype(1);

        String ExpectedVal = "{\"name\" : \"" + user.getName() +
                "\",\"gamertag\" : \"" + user.getGamertag() +
                "\",\"id\" : \"" + user.getId() +
                "\", \"dateofbirth\" : \"" + user.getDateofbirth() +
                "\",\"password\" : \"" + user.getPassword() +
                "\",\"sex\" : \"" + user.getSex() +
                "\", \"email\" : \"" + user.getEmail() +
                "\",\"console\" : \"" + user.getConsole() + "\"}";

        JSONObject expectedJSON = null;
        try{
             expectedJSON = new JSONObject(ExpectedVal);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        Mockito.when(user.toJSON()).thenReturn(expectedJSON);
        assertEquals(expectedJSON, user.toJSON());
    }



}
