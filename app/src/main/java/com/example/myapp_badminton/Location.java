package com.example.myapp_badminton;

import java.util.List;

public class Location {
     class State implements Comparable<State> {

        private int stateID;
        private String stateName;

        public State(int stateID, String stateName) {
            this.stateID = stateID;
            this.stateName = stateName;
        }

        public int getStateID() {
            return stateID;
        }

        public String getStateName() {
            return stateName;
        }

        @Override
        public String toString() {
            return stateName;
        }

        @Override
        public int compareTo(State another) {
            return this.getStateID() - another.getStateID();//ascending order
//            return another.getStateID()-this.getStateID();//descending order
        }
    }


    public class City implements Comparable<City> {

        private int cityID;
        private State state;
        private String cityName;

        public City(int cityID, State state, String cityName) {
            this.cityID = cityID;
            this.state = state;
            this.cityName = cityName;
        }

        public int getCityID() {
            return cityID;
        }


        public State getState() {
            return state;
        }

        public String getCityName() {
            return cityName;
        }

        @Override
        public String toString() {
            return cityName;
        }

        @Override
        public int compareTo(City another) {
            return this.cityID - another.getCityID();//ascending order
//            return another.getCityID() - this.cityID;//descending order
        }
    }
    public class LocalLocation implements Comparable<LocalLocation> {

        private int locationID;
        private State state;
        private City city;
        private String locationName;

        public LocalLocation(int locationID, State state, City city, String locationName) {
            this.locationID = locationID;
            this.state = state;
            this.city = city;
            this.locationName = locationName;
        }

        public int getLocationID() {
            return locationID;
        }

        public void setLocationID(int locationID) {
            this.locationID = locationID;
        }

        public State getState() {
            return state;
        }

        public void setState(State state) {
            this.state = state;
        }

        public City getCity() {
            return city;
        }

        public void setCity(City city) {
            this.city = city;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        @Override
        public int compareTo(LocalLocation another) {
            return this.locationID - another.getLocationID();//ascending order
//            return another.getCityID() - this.cityID;//descending order
        }
    }
    private void createLists() {
        Country country0 = new Country(0, "Choose a Country");
        Country country1 = new Country(1, "Country1");
        Country country2 = new Country(2, "Country2");

        countries.add(new Country(0, "Choose a Country"));
        countries.add(new Country(1, "Country1"));
        countries.add(new Country(2, "Country2"));

        State state0 = new State(0, country0, "Choose a Country");
        State state1 = new State(1, country1, "state1");
        State state2 = new State(2, country1, "state2");
        State state3 = new State(3, country2, "state3");
        State state4 = new State(4, country2, "state4");

        states.add(state0);
        states.add(state1);
        states.add(state2);
        states.add(state3);
        states.add(state4);

        cities.add(new City(0, country0, state0, "Choose a City"));
        cities.add(new City(1, country1, state1, "City1"));
        cities.add(new City(2, country1, state1, "City2"));
        cities.add(new City(3, country1, state2, "City3"));
        cities.add(new City(4, country2, state2, "City4"));
        cities.add(new City(5, country2, state3, "City5"));
        cities.add(new City(6, country2, state3, "City6"));
        cities.add(new City(7, country2, state4, "City7"));
        cities.add(new City(8, country1, state4, "City8"));
    }

}
