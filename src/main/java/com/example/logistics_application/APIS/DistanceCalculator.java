//package com.example.logistics_application.APIS;
//
//import org.json.JSONObject;
//import org.springframework.web.client.RestTemplate;
//
//public class DistanceCalculator {
//    private static final String API_KEY = "AIzaSyDdWk5PLvjAuURJKy61pcPG-0zvjqtzCUg";
//
//    public static double getDistance(String pickupLoc, String dropLoc) {
//        try {
//            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="
//                    + pickupLoc + "&destinations=" + dropLoc + "&key=" + API_KEY;
//
//            RestTemplate restTemplate = new RestTemplate();
//            String response = restTemplate.getForObject(url, String.class);
//            System.out.println(url);
//            JSONObject jsonObject = new JSONObject(response);
//            double distanceInMeters = jsonObject.getJSONArray("rows")
//                    .getJSONObject(0)
//                    .getJSONArray("elements")
//                    .getJSONObject(0)
//                    .getJSONObject("distance")
//                    .getDouble("value");
//
//            return distanceInMeters / 1000; // Convert meters to km
//        } catch (Exception e) {
//            e.printStackTrace();
//            return -1; // Return -1 if an error occurs
//
//        }
//    }
//}
//
//
//
//
