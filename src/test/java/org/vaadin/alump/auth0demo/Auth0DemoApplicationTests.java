package org.vaadin.alump.auth0demo;

import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Auth0DemoApplicationTests {

	@Autowired
	private Auth0Management mgt;

	@Test
	public void contextLoads() throws Auth0Exception {

		if(!mgt.isEnabled()) {
			// No management API key, just skip
			return;
		}

		List<User> users = mgt.getUsers();

		for (User user : users) {
			System.out.println("Found user " + user.getEmail() + " " + user.getId());

			User receivedUser = mgt.getUser(user.getId()).get();
			Assert.assertEquals(user.getId(), receivedUser.getId());
		}
	}

}
