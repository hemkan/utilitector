import streamlit as st
import pandas as pd
import pydeck as pdk
import json

# TODO: initialize this 
# if st.session_state["authenticated"]:
#     st.write("## Welcome to the Dashboard")
# else:
#     st.switch_page('./auth.py')


st.write("# Dashboard")

# TODO: profile info?

json_data = '''[
    {"GeoLocationCoordinates": {"latitude": 37.76, "longitude": -122.4}, "type": "Earthquake", "description": "Golden Gate Park, a large urban park with gardens, trails, and recreational areas."},
    {"GeoLocationCoordinates": {"latitude": 37.78, "longitude": -122.42}, "type": "Earthquake", "description": "The Exploratorium, a museum of science, art, and human perception."},
    {"GeoLocationCoordinates": {"latitude": 37.75, "longitude": -122.43}, "type": "Earthquake", "description": "Scoma's Restaurant, known for its seafood and waterfront dining."},
    {"GeoLocationCoordinates": {"latitude": 37.77, "longitude": -122.41}, "type": "Flood", "description": "The Painted Ladies, a row of colorful Victorian houses."},
    {"GeoLocationCoordinates": {"latitude": 37.79, "longitude": -122.45}, "type": "Fire", "description": "Lombard Street, famous for its steep, winding road."},
    {"GeoLocationCoordinates": {"latitude": 37.74, "longitude": -122.39}, "type": "Fire", "description": "Ferry Building Marketplace, offering local food and goods."},
    {"GeoLocationCoordinates": {"latitude": 37.72, "longitude": -122.41}, "type": "Tornado", "description": "Ocean Beach, a long beach with scenic views and surf."},
    {"GeoLocationCoordinates": {"latitude": 37.76, "longitude": -122.38}, "type": "Hurricane", "description": "Twin Peaks, offering panoramic views of the city."},
    {"GeoLocationCoordinates": {"latitude": 37.71, "longitude": -122.43}, "type": "Hurricane", "description": "The Castro Theatre, a historic movie palace."},
    {"GeoLocationCoordinates": {"latitude": 37.75, "longitude": -122.36}, "type": "Other", "description": "The San Francisco Museum of Modern Art, featuring contemporary artworks."}
]'''

data = json.loads(json_data)

chart_data = pd.DataFrame(data)

chart_data['lat'] = chart_data['GeoLocationCoordinates'].apply(lambda x: x['latitude'])
chart_data['lon'] = chart_data['GeoLocationCoordinates'].apply(lambda x: x['longitude'])

colors = {
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
}

chart_data['color'] = chart_data['type'].apply(lambda x: colors[x])

st.write("### Reported Incident Locations")
# monitoringLocation = "Alabama"

# # TODO: endpoint for getting UserData.monitoring
# st.write(f"This map shows the locations of reported incidents in {monitoringLocation}.")

st.pydeck_chart(
    pdk.Deck(
        map_style=None,
        initial_view_state=pdk.ViewState(
            latitude=37.76,
            longitude=-122.4,
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