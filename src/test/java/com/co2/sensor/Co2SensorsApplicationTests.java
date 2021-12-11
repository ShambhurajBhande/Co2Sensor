package com.co2.sensor;

import com.co2.sensor.model.SensorDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class Co2SensorsApplicationTests {

	private String getRootUrl() {
		return "http://localhost:" + 8080;
	}
	TestRestTemplate restTemplate = new TestRestTemplate();

	private ResponseEntity<String> saveMeasurement(String uuid,int co2Level) {
		SensorDTO sensorDTO=new SensorDTO(co2Level,new Date());
		ResponseEntity<String> response = restTemplate.postForEntity(
			getRootUrl() + "/api/v1/sensors/"+uuid+"/measurements", sensorDTO,
			String.class);
		return response;
	}

	private ResponseEntity<String> getSensor(String uuid){

		return restTemplate
			.getForEntity(getRootUrl() + "/api/v1/sensors/"+uuid, String.class);

	}

	@Test
	public void testCreateMeasurementWithSuccess() throws JSONException {
		SensorDTO SensorDTO = new SensorDTO(
			1000, new Date());
		ResponseEntity<String> response = saveMeasurement("bbcf0146-e326-4b90-af0e-309a194d4a9b",1000);
		Assert.assertTrue(response.getStatusCodeValue() == 201);
	}

	@Test
	public void testCo2SensorWithStatusOK() throws JSONException {
		saveMeasurement("bbcf0146-e326-4b90-af0e-309a194d4a9c",150);
		saveMeasurement("bbcf0146-e326-4b90-af0e-309a194d4a9c",300);
		ResponseEntity<String> response =getSensor("bbcf0146-e326-4b90-af0e-309a194d4a9c");
		JSONObject jsonObj = getSensorStatus(response);
		Assert.assertEquals(jsonObj.get("status"), "OK");
	}


	@Test
	public void testCo2SensorWithStatusWARN() throws JSONException {
		saveMeasurement("bbcf0146-e326-4b90-af0e-309a194d4a9d",5000);
		ResponseEntity<String> response =getSensor("bbcf0146-e326-4b90-af0e-309a194d4a9d");
		JSONObject jsonObj = getSensorStatus(response);
		Assert.assertEquals(jsonObj.get("status"), "WARN");

	}

	@Test
	public void testCo2SensorWithStatusALERT() throws JSONException {
		saveMeasurement("bbcf0146-e326-4b90-af0e-309a194d4a9e",5001);
		saveMeasurement("bbcf0146-e326-4b90-af0e-309a194d4a9e",5002);
		saveMeasurement("bbcf0146-e326-4b90-af0e-309a194d4a9e",5003);
		ResponseEntity<String> response =getSensor("bbcf0146-e326-4b90-af0e-309a194d4a9e");
		JSONObject jsonObj = getSensorStatus(response);
		Assert.assertEquals(jsonObj.get("status"), "ALERT");

	}

	@Test
	public void testCo2SensorWithStatusFromOkToWARN() throws JSONException {
		saveMeasurement("bbcf0146-e326-4b90-af0e-309a194d4a9f",1000);
		ResponseEntity<String> response =getSensor("bbcf0146-e326-4b90-af0e-309a194d4a9f");

		JSONObject jsonObj = getSensorStatus(response);
		Assert.assertEquals(jsonObj.get("status"), "OK");

		saveMeasurement("bbcf0146-e326-4b90-af0e-309a194d4a9f",3000);
		ResponseEntity<String> wresponse =getSensor("bbcf0146-e326-4b90-af0e-309a194d4a9f");
		JSONObject jsonObj2 = getSensorStatus(wresponse);
		Assert.assertEquals(jsonObj2.get("status"), "WARN");
	}


	private JSONObject getSensorStatus(ResponseEntity<String> response) throws JSONException
	{
		Assert.assertTrue(response.getStatusCodeValue() == 200);
		return new JSONObject(response.getBody());
	}

	@Test
	public void testCo2SensorWithStatusFromWarnToOk() throws JSONException {
		saveMeasurement("383fa07e-9758-47de-bdac-63e272134b79",3000);
		ResponseEntity<String> response =getSensor("383fa07e-9758-47de-bdac-63e272134b79");
		JSONObject jsonObj = getSensorStatus(response);
		Assert.assertEquals(jsonObj.get("status"), "WARN");

		saveMeasurement("383fa07e-9758-47de-bdac-63e272134b79",1000);
		saveMeasurement("383fa07e-9758-47de-bdac-63e272134b79",1100);
		saveMeasurement("383fa07e-9758-47de-bdac-63e272134b79",1200);

		ResponseEntity<String> response1 =getSensor("383fa07e-9758-47de-bdac-63e272134b79");
		JSONObject jsonObj1 = getSensorStatus(response1);
		Assert.assertEquals(jsonObj1.get("status"), "OK");

	}

	@Test
	public void testCo2SensorWithStatusFromWarnToAlert() throws JSONException {
		saveMeasurement("18a6efc0-4c3f-44bf-921a-a49db5f83119",3000);
		ResponseEntity<String> response =getSensor("18a6efc0-4c3f-44bf-921a-a49db5f83119");
		JSONObject jsonObj = getSensorStatus(response);
		Assert.assertEquals(jsonObj.get("status"), "WARN");

		saveMeasurement("18a6efc0-4c3f-44bf-921a-a49db5f83119",3000);
		saveMeasurement("18a6efc0-4c3f-44bf-921a-a49db5f83119",3000);
		ResponseEntity<String> response1 =getSensor("18a6efc0-4c3f-44bf-921a-a49db5f83119");
		JSONObject jsonObj1 = getSensorStatus(response1);
		Assert.assertEquals(jsonObj1.get("status"), "ALERT");
	}

	@Test
	public void testCo2SensorWithStatusFromOkToAlert() throws JSONException {
		saveMeasurement("76160319-1411-4d72-b095-d1cd9200dc0a",1000);
		ResponseEntity<String> response =getSensor("76160319-1411-4d72-b095-d1cd9200dc0a");
		JSONObject jsonObj = getSensorStatus(response);
		Assert.assertEquals(jsonObj.get("status"), "OK");

		saveMeasurement("76160319-1411-4d72-b095-d1cd9200dc0a",4000);
		saveMeasurement("76160319-1411-4d72-b095-d1cd9200dc0a",4000);
		saveMeasurement("76160319-1411-4d72-b095-d1cd9200dc0a",4000);
		ResponseEntity<String> response1 =getSensor("76160319-1411-4d72-b095-d1cd9200dc0a");
		JSONObject jsonObj1 = getSensorStatus(response1);
		Assert.assertEquals(jsonObj1.get("status"), "ALERT");
	}

	@Test
	public void testCo2SensorWithStatusFromAlertToOk() throws JSONException {
		saveMeasurement("b04ce81a-b123-44e8-be96-300f603393ae",3000);
		saveMeasurement("b04ce81a-b123-44e8-be96-300f603393ae",4000);
		saveMeasurement("b04ce81a-b123-44e8-be96-300f603393ae",5000);
		ResponseEntity<String> response =getSensor("b04ce81a-b123-44e8-be96-300f603393ae");
		JSONObject jsonObj = getSensorStatus(response);
		Assert.assertEquals(jsonObj.get("status"), "ALERT");

		saveMeasurement("b04ce81a-b123-44e8-be96-300f603393ae",1000);
		saveMeasurement("b04ce81a-b123-44e8-be96-300f603393ae",1000);
		saveMeasurement("b04ce81a-b123-44e8-be96-300f603393ae",1000);
		ResponseEntity<String> response1 =getSensor("b04ce81a-b123-44e8-be96-300f603393ae");
		JSONObject jsonObj1 = getSensorStatus(response1);
		Assert.assertEquals(jsonObj1.get("status"), "OK");
	}

	@Test
	public void testCo2SensorAlerts() throws JSONException {
		saveMeasurement("f9a01ab4-1625-4c4c-a54f-405a58daba30",5000);
		saveMeasurement("f9a01ab4-1625-4c4c-a54f-405a58daba30",6000);
		saveMeasurement("f9a01ab4-1625-4c4c-a54f-405a58daba30",7000);
		ResponseEntity<String> response =getSensor("f9a01ab4-1625-4c4c-a54f-405a58daba30");
		JSONObject jsonObj1 = getSensorStatus(response);
		Assert.assertEquals(jsonObj1.get("status"), "ALERT");

		ResponseEntity<String> alertsResponse = restTemplate.getForEntity(
			getRootUrl() + "/api/v1/sensors/f9a01ab4-1625-4c4c-a54f-405a58daba30/alerts", String.class);

		JSONArray jsonArr = new JSONArray(alertsResponse.getBody());
		JSONObject jsonObj = (JSONObject) jsonArr.get(0);
		Assert.assertTrue(alertsResponse.getStatusCodeValue() == 200);

		Assert.assertEquals(jsonObj.get("measurement1"), "6000");
		Assert.assertEquals(jsonObj.get("measurement2"), "5000");
		Assert.assertEquals(jsonObj.get("measurement3"), "7000");

	}


	@Test
	public void testCo2SensorMetrics() throws JSONException {
		saveMeasurement("a10d4f20-bd64-43f4-8ab5-6bcdcbbf2108",5000);
		saveMeasurement("a10d4f20-bd64-43f4-8ab5-6bcdcbbf2108",1000);
		saveMeasurement("a10d4f20-bd64-43f4-8ab5-6bcdcbbf2108",500);
		saveMeasurement("a10d4f20-bd64-43f4-8ab5-6bcdcbbf2108",500);
		saveMeasurement("a10d4f20-bd64-43f4-8ab5-6bcdcbbf2108",5500);

		ResponseEntity<String> response = restTemplate.getForEntity(
			getRootUrl() + "/api/v1/sensors/a10d4f20-bd64-43f4-8ab5-6bcdcbbf2108/metrics", String.class);
		Assert.assertTrue(response.getStatusCodeValue() == 200);

		JSONObject jsonObj = new JSONObject(response.getBody());
		Assert.assertEquals(jsonObj.get("maxLast30Days"), "5500");
		Assert.assertEquals(jsonObj.get("avgLast30Days"), "2500");

	}

	@Test
	public void testCo2dSensorWithNoMetrics() throws JSONException
	{
		ResponseEntity<String> response = restTemplate.getForEntity(
			getRootUrl() + "/api/v1/sensors/c945ea51-5c9d-476e-9cbb-3dbf88115975/metrics", String.class);
		Assert.assertTrue(response.getStatusCodeValue() == 200);
		JSONObject jsonObj = new JSONObject(response.getBody());
		Assert.assertEquals(jsonObj.toString(), "{}");
	}

	@Test
	public void testCreateMeasurementWithoutDate() throws JSONException {

		SensorDTO sensorDTO = new SensorDTO();
		sensorDTO.setCo2Level(1000);
		ResponseEntity<String> response = restTemplate.postForEntity(
			getRootUrl() + "/api/v1/sensors/2b12dcb0-6d4c-467d-b852-87660ce2a5e3/measurements", sensorDTO,
			String.class);
		Assert.assertTrue(response.getStatusCodeValue() == 400);
		JSONObject jsonObj = new JSONObject(response.getBody());
		Assert.assertEquals(jsonObj.get("date"), "Sensor date is mandatory");
	}

}
