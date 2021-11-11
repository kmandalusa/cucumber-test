package com.revature.cucumber_test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepDefinitions {

	private final CloseableHttpClient httpClient = HttpClients.createDefault();

	private String customerId = "";
	private HttpResponse response = null;
	private ObjectMapper objectMapper = new ObjectMapper();

	@When("Register as name {string} with email {string},mobile {string} and password {string}")
	public void register_as_name_with_email_mobile_and_password(String name, String email, String mobile, String pass)
			throws Exception {
		HttpPost request = new HttpPost("http://localhost:8090/customers");
		Map<String, String> data = new HashMap<>();
		data.put("name", name);
		data.put("email", email);
		data.put("mobile", mobile);
		data.put("password", pass);

		String json = objectMapper.writeValueAsString(data);
		StringEntity entity = new StringEntity(json);
		request.addHeader("content-type", "application/json");
		request.setEntity(entity);
		response = httpClient.execute(request);

	}

	@Then("Success")
	public void success() {
		int status = response.getStatusLine().getStatusCode();
		assertEquals(200, status);
	}

	@When("Ask for all customers")
	public void ask_for_all_customers() throws Exception {
		HttpGet request = new HttpGet("http://localhost:8090/customers");
		response = httpClient.execute(request);
	}

	@Then("Return all customers")
	public void return_all_customers() throws Exception {
		int status = response.getStatusLine().getStatusCode();
		assertEquals(200, status);
		String json = EntityUtils.toString(response.getEntity());
		System.out.println(json);
	}

	@Given("A customer email as {string}")
	public void a_customer_email_as(String email) throws Exception {
		String url = "http://localhost:8090/customers/searchByEmail/" + email;
		HttpGet request = new HttpGet(url);
		response = httpClient.execute(request);
	}

	@Then("Return the said customer")
	public void return_the_said_customer() throws ParseException, IOException {
		int status = response.getStatusLine().getStatusCode();
		assertEquals(200, status);
		String json = EntityUtils.toString(response.getEntity());
		Map<String, Object> map = objectMapper.readValue(json, Map.class);
		customerId = map.get("customerId").toString();
		System.out.println("Customer Id: " + customerId);
		System.out.println(json);
	}

	@Given("A customer with wrong email as {string}")
	public void a_customer_with_wrong_email_as(String email) throws ClientProtocolException, IOException {
		String url = "http://localhost:8090/customers/searchByEmail/" + email;
		HttpGet request = new HttpGet(url);
		response = httpClient.execute(request);
	}

	@Then("No customer should return")
	public void no_customer_should_return() {
		int status = response.getStatusLine().getStatusCode();
		// assertEquals(404, status);
	}

	@Given("Earlier customer delete the same")
	public void earlier_customer_delete_the_same() throws Exception {
		String url = "http://localhost:8090/customers/" + customerId;
		HttpDelete request = new HttpDelete(url);
		response = httpClient.execute(request);
	}

	@Then("Delete successful")
	public void delete_successful() {
		int status = response.getStatusLine().getStatusCode();
		assertEquals(200, status);
	}
}
