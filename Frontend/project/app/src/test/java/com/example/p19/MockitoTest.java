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

//Author - Joe Dobosenski

@RunWith(MockitoJUnitRunner.class)
public class MockitoTest {


    @Test
    public void toJsonStringTest(){

        User user = Mockito.mock(User.class);


        user.setEmail("names@gmail.com");
        user.setConsole("PC");
        user.setSex("FEmale");
        user.setDateofbirth("01/05/1997");
        user.setGamertag("namtage");
        user.setName("nmae");
        user.setPassword("100000");
        user.setUsertype(1);

        String ExpectedVal = "{\"name\" : \"" + user.getName() +
                "\",\"gamertag\" : \"" + user.getGamertag() +
                "\",\"id\" : \"" + user.getId() +
                "\", \"dateofbirth\" : \"" + user.getDateofbirth() +
                "\",\"password\" : \"" + user.getPassword() +
                "\",\"sex\" : \"" + user.getSex() +
                "\", \"email\" : \"" + user.getEmail() +
                "\",\"console\" : \"" + user.getConsole() + "\"}";

        Mockito.when(user.toJsonString()).thenReturn(ExpectedVal);
        assertEquals(ExpectedVal, user.toJsonString());
    }

    @Test
    public void emailRegexTest(){
        LoginActivity loginTest = Mockito.mock(LoginActivity.class);
        String emailTest = "this.is@worng@email";

        Mockito.when(loginTest.validEmail(emailTest)).thenReturn(false);
        assertEquals(false, loginTest.validEmail(emailTest));

        emailTest = "this@email";
        Mockito.when(loginTest.validEmail(emailTest)).thenReturn(true);
        assertEquals(true, loginTest.validEmail(emailTest));
    }

    @Test
    public void getAndSetNameTest(){
        User userTest = Mockito.mock(User.class);
        String Name = "joe dobo";
        userTest.setName(Name);
        Mockito.when(userTest.getName()).thenReturn(Name);
        assertEquals(Name, userTest.getName());
    }
}
