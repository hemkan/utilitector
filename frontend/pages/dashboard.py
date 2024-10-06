from collections import defaultdict

import requests
import streamlit as st
import pandas as pd
import pydeck as pdk
import json
import pycountry
from geopy import Nominatim, Location
from geopy.extra.rate_limiter import RateLimiter
from streamlit_cookies_controller import CookieController

st.set_page_config(layout="wide")


if 'loc_country' not in st.session_state:
  st.session_state.loc_country = None

# TODO: initialize this 
# if st.session_state["authenticated"]:
#     st.write("## Welcome to the Dashboard")
# else:
#     st.switch_page('./auth.py')


st.write("# Dashboard")

# TODO: profile info?

@st.cache_data
def getData():
    res = requests.get("http://localhost:8080/api/analysis/all", verify=False)
    print(res.content)
    return res.json()['locations']


data = getData()
# json_data = '''[
#     {"GeoLocationCoordinates": {"latitude": 37.76, "longitude": -122.4}, "type": "Earthquake", "description": "Golden Gate Park, a large urban park with gardens, trails, and recreational areas."},
#     {"GeoLocationCoordinates": {"latitude": 37.78, "longitude": -122.42}, "type": "Earthquake", "description": "The Exploratorium, a museum of science, art, and human perception."},
#     {"GeoLocationCoordinates": {"latitude": 37.75, "longitude": -122.43}, "type": "Earthquake", "description": "Scoma's Restaurant, known for its seafood and waterfront dining."},
#     {"GeoLocationCoordinates": {"latitude": 37.77, "longitude": -122.41}, "type": "Flood", "description": "The Painted Ladies, a row of colorful Victorian houses."},
#     {"GeoLocationCoordinates": {"latitude": 37.79, "longitude": -122.45}, "type": "Fire", "description": "Lombard Street, famous for its steep, winding road."},
#     {"GeoLocationCoordinates": {"latitude": 37.74, "longitude": -122.39}, "type": "Fire", "description": "Ferry Building Marketplace, offering local food and goods."},
#     {"GeoLocationCoordinates": {"latitude": 37.72, "longitude": -122.41}, "type": "Tornado", "description": "Ocean Beach, a long beach with scenic views and surf."},
#     {"GeoLocationCoordinates": {"latitude": 37.76, "longitude": -122.38}, "type": "Hurricane", "description": "Twin Peaks, offering panoramic views of the city."},
#     {"GeoLocationCoordinates": {"latitude": 37.71, "longitude": -122.43}, "type": "Hurricane", "description": "The Castro Theatre, a historic movie palace."},
#     {"GeoLocationCoordinates": {"latitude": 37.75, "longitude": -122.36}, "type": "Other", "description": "The San Francisco Museum of Modern Art, featuring contemporary artworks."}
# ]'''

# data = json.loads(json_data)

chart_data = pd.DataFrame(data)

chart_data['lat'] = chart_data['location'].apply(lambda x: x['latitude'])
chart_data['lon'] = chart_data['location'].apply(lambda x: x['longitude'])

colors = defaultdict(lambda: [255,0,0], {
    "Earthquake": [255, 0, 0],
    "Flood": [0, 0, 255],
    "Fire": [255, 255, 0],
    "Tornado": [0, 255, 0],
    "Hurricane": [255, 0, 255],
    "Other": [0, 255, 255],
    "Internet": [120,89,0],
    "Tremors": [10,78,98],
    "Electricity": [45,97,100],
    "Sewage": [0,68,9],
    "Gas": [87,90,90],
    
    #"Internet", "Water", "Tremors", "Electricity", "Sewage", "Gas", "Other"
})

chart_data['color'] = chart_data['type'].apply(lambda x: colors[x])

print(chart_data)

st.write("### Reported Incident Locations")
# monitoringLocation = "Alabama"

# # TODO: endpoint for getting UserData.monitoring
# st.write(f"This map shows the locations of reported incidents in {monitoringLocation}.")



controller = CookieController()
st.session_state.storedid = controller.get("storedid")
if st.session_state.storedid == None:
    st.session_state.storedid = 0

st.session_state.storedtoken = controller.get("storedtoken")
if st.session_state.storedtoken == None:
    st.session_state.storedtoken = ""




st.pydeck_chart(
    pdk.Deck(
        map_style=None,
        initial_view_state=pdk.ViewState(
            # latitude=37.76,
            # longitude=-122.4,
            latitude=40.0,
            longitude=40.0,
            zoom=11,
            pitch=50,
        ),
        layers=[
            pdk.Layer(
                "ScatterplotLayer",
                data=chart_data,
                get_position="[lon, lat]",
                get_fill_color="color",
                opacity = .05,
                width = 500,
                get_height = "height",
                get_radius=2000, 
                pickable=True,
            ),
        ],
        tooltip={"text": "{type}"},
    ),
    height=550,
)

# display in table format - debug
# chart_data['latitute'] = chart_data['lat']
# chart_data['longitude'] = chart_data['lon']
# st.write(chart_data[['type', 'description', 'latitute', 'longitude']])

for index, row in chart_data.iterrows():
    with st.expander(f"{row['type']} at lat: {row['lat']}, lon: {row['lon']}"): # EXTRA: reverse geocode to get real location

        st.write(f"**Type:** {row['type']}")
        st.write(f"**Description:** {row['description']}")
        st.write(f"**Location:** ({row['lat']}, {row['lon']})")

@st.fragment
def locationForm():
    if 'loc_location' not in st.session_state:
        st.session_state.loc_location = None
    
    def getCountries():
        return pycountry.countries
    
    def getSubdivisions():
        return pycountry.subdivisions
    
    
    @st.cache_data
    def cached_countrySorted():
        l = sorted([el.name for el in iter(getCountries())])
        l.remove('United States')
        l.insert(0, 'United States')
        return l
    
    st.subheader("Location")
    street = st.text_input("Street")
    city = st.text_input("City")
    
    @st.fragment
    def frag_stateProvince():
        country = getCountries().get(name=st.session_state.loc_country)
        print(country)
        subdivisions: set[any] = getSubdivisions().get(country_code=country.alpha_2)
        if subdivisions:
            print(subdivisions)
            return st.selectbox(
                "State/Province",
                options=sorted([el.name for el in subdivisions]),
                key='loc_stateprovince'
            )
    
    province = frag_stateProvince() if (st.session_state.loc_country) else ""
    country = st.selectbox(
        "Country",
        options=cached_countrySorted(),
        key='loc_country'
        # index=0,
        # placeholder="United States"
    )
    def getLatLng(street, city, state_province, country):
        geolocator = Nominatim(user_agent="GTA Lookup")
        geocode = RateLimiter(geolocator.geocode, min_delay_seconds=1)
        loc: Location = geocode(street + ", "
                                + city + ", "
                                + ((state_province + ", ") if state_province else "")
                                + country)
        print(loc.point)  # TODO remove after debug
        return {'latitude': loc.latitude, 'longitude': loc.longitude}
    
    return lambda: getLatLng(street, city, province, country)
if st.button("Subscribe"):
    locationForm()
    if st.button("Confirm"):
        sub = requests.get(data=locationForm)
