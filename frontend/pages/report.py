import streamlit as st
import json
import requests 
# import googlemaps
# from streamlit_geolocation import streamlit_geolocation

# with open('../pages/config.yaml', 'r', encoding='utf-8') as file:
#     config = yaml.load(file, Loader=yaml.SafeLoader)

# gmaps = googlemaps.Client(config['google']['api_key'])
# location = streamlit_geolocation()

if 'form' not in st.session_state:
    st.session_state.form = False

if 'agent' not in st.session_state:
    st.session_state.agent = False

if 'messages' not in st.session_state:
    st.session_state.messages = []

st.write("# Report")

if 'chat_id' not in st.session_state:
    st.session_state.chat_id = None

if st.button("File a Report Manually"):
    st.session_state.form = True
    st.session_state.agent = False

if st.button("Help from an Agent"):
    st.session_state.agent = True
    st.session_state.form = False

    # TODO: endpoint for get a chat_id
    st.session_state.chat_id = requests.post("http://localhost:8080/api/bot/new-chat").json()["id"]
    st.session_state.first_message = requests.post("http://localhost:8080/api/bot/new-chat").json()["firstMessage"]
    # chat_id = requests.post("http://localhost:8080/bot/new-chat").json()["id"]
    st.write(st.session_state.chat_id)
    st.write(st.session_state.first_message)



if st.session_state.get("agent"):
    st.write("# Talk to Despair")
    
    # message history
    st.write(f"Despair: {st.session_state.first_message}")
    for message in st.session_state.messages:
        st.write(message)

    user_message = st.text_input("Message")

    if st.button("Send"):
        if user_message != "":
            st.session_state.messages.append(f"You: {user_message}")
            message = json.dumps({"id": st.session_state.chat_id, "content": user_message})
            # st.write(message)
            response = requests.post("http://localhost:8080/api/bot/message", data=message, headers={"Content-Type": "application/json"})
            st.session_state.messages.append(response.json()["content"])
            st.session_state.user_input = ""


if st.session_state.get("form"):
    st.write("# File a Report")

    location = st.text_input("Location")


    # # TODO: ask user for enter location manually (google maps/api for similar format) or use geolocation
    # st.selectbox("Location", ["Current Location", ____])

    type = st.selectbox("Type", ["Electricity", "Water", "Gas", "Flooding", "Tornado", "Sewage", "Tremors", "Internet", "Other"])
    description = st.text_area("Description")
    
    if st.button("Submit"):
        report_data = {
            "location": location,
            "type": type,
            "description": description
        }

        report_json = json.dumps(report_data)
        st.write(report_json)

        # TODO: endpoint w report_json
        response = requests.post("http://localhost:8080/api/report/submit", data=report_json, headers={"Content-Type": "application/json"})
        st.write(response)


