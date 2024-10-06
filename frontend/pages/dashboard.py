import streamlit as st
import pandas as pd
import pydeck as pdk
import json

st.write("## Welcome To Dashboard")
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

chat_data = pd.DataFrame(data)

chat_data['lat'] = chat_data['GeoLocationCoordinates'].apply(lambda x: x['latitude'])
chat_data['lon'] = chat_data['GeoLocationCoordinates'].apply(lambda x: x['longitude'])

colors = {
    "Earthquake": [255, 0, 0],
    "Flood": [0, 0, 255],
    "Fire": [255, 255, 0],
    "Tornado": [0, 255, 0],
    "Hurricane": [255, 0, 255],
    "Other": [0, 255, 255],
}

chat_data['color'] = chat_data['type'].apply(lambda x: colors[x])

st.write("## Reported Incident Locations")
monitoringLocation = "Alabama"

# TODO: endpoint for getting UserData.monitoring
st.write(f"This map shows the locations of reported incidents in {monitoringLocation}.")

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
                data=chat_data,
                get_position="[lon, lat]",
                get_fill_color="color",
                get_radius=200, 
                pickable=True,
            ),
        ],
        tooltip={"text": "{type}"},
    ),
    height=550,
)



