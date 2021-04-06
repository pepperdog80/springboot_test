/*package com.example.springboot;

import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationTest {

}

 */
package com.example.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPostAndDelete() throws Exception {

        //this goes to /api and posts the string "stringToTest"
        mockMvc.perform(MockMvcRequestBuilders.post("/api?post_input_text=stringToTest")).andReturn();

        //this says if "stringToTest" is there, then delete it
        mockMvc.perform(MockMvcRequestBuilders.post("/delete?post_text=stringToTest")).andReturn();

        //this tries to delete the string "stringToTest" again but since it shouldn't be
        //there we expect the content to contain "does not exist"
        mockMvc.perform(MockMvcRequestBuilders.post("/delete?post_text=stringToTest").contentType(MediaType.ALL))
                .andExpect(content().string(containsString("does not exist")));
    }

    @Test
    void testCaps() throws Exception {

        //posts teststring
        mockMvc.perform(MockMvcRequestBuilders.post("/api?post_input_text=teststring")).andReturn();

        //this deletes teststring assuming delete is NOT case sensitive
        mockMvc.perform(MockMvcRequestBuilders.post("/delete?post_text=TESTSTRING")).andReturn();

        //this looks in history for teststring to see if delete was actally case
        //sensitive or not
        mockMvc.perform(MockMvcRequestBuilders.get("/history").contentType(MediaType.ALL))
                .andExpect(content().string(containsString("teststring")));

        //since this test works, it shows that "teststring" is still in the history
        //it means that calling delete on TESTSTRING did NOT delete teststring
        //which therefore shows that delete is case sensitive
    }
}