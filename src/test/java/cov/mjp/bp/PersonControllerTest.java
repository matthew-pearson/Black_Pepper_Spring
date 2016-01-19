package cov.mjp.bp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import cov.mjp.bp.controller.PersonController;
import cov.mjp.bp.model.Person;

public class PersonControllerTest {
	
	private static final MediaType FORM_URL = MediaType.parseMediaType("application/x-www-form-urlencoded");
	private static final MediaType JSON_UTF8 = MediaType.parseMediaType("application/json;charset=UTF-8");

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new PersonController()).build();
    }

    @Test
    public void testPostAndGet() throws Exception {
        mockMvc.perform(
				post("/")
				.contentType(FORM_URL)
				.param("forename", "Buzz")
				.param("surname", "Aldrin")
				.param("age", "85"))
	    	.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isCreated());
        
        mockMvc.perform(
        		get("/")
				.contentType(JSON_UTF8))
    		.andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.forename", is("Buzz")))
            .andExpect(jsonPath("$.surname", is("Aldrin")))
            .andExpect(jsonPath("$.age", is(85)));
    }
}
