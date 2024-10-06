import json

import requests
import streamlit as st
from py.fragments import locationForm

# initialize session state things
for (state_name, default) in (
		('form', False),
		('agent', False),
		('messages', []),
		('loc_country', None),
		('first_message', "Hello, how can I help you today?"),
		('chat_id', None)
):
	if state_name not in st.session_state:
		st.session_state[state_name] = default


def frag_formComponent():
	st.write("## File a Report")

	func_resolveLocation = locationForm()
	
	
	type = st.selectbox("Type", ["Flood", "Fire", "Tornado", "Internet", "Water", "Tremors", "Electricity", "Sewage", "Gas", "Other"])
	
	description = st.text_area("Description")
	
	if st.button("Submit"):
		report_data = {
			"location": func_resolveLocation(),
			"type": type,
			"description": description
		}
		
		report_json = json.dumps(report_data)
		st.write(report_json)
		
		try:
			response = requests.post("http://localhost:8080/api/report/submit", data=report_json, headers={"Content-Type": "application/json"})
			st.success("Report submitted")
		except:
			st.error("Failed to submit report")


# import googlemaps
# from streamlit_geolocation import streamlit_geolocation
# with open('../pages/config.yaml', 'r', encoding='utf-8') as file:
#     config = yaml.load(file, Loader=yaml.SafeLoader)
# gmaps = googlemaps.Client(config['google']['api_key'])
# location = streamlit_geolocation()


button_height = "40px" if st.session_state.form or st.session_state.agent else "40vh"

st.markdown(
	f"""
    <style>
    .stButton>button {{
        height: {button_height};
        font-size: 16px;
    }}
    </style>
    """,
	unsafe_allow_html=True
)

st.write("# Report")
st.write("**If you are in immediate danger, please call 911.**")
st.write("*Please select an option below to file a report or get help from an agent.*")

left, right = st.columns(2)
if left.button("File a Report Manually", use_container_width=True):
	st.session_state.form = True
	st.session_state.agent = False
	st.rerun()

if right.button("Help from an Agent", use_container_width=True):
	st.session_state.agent = True
	st.session_state.form = False
	st.session_state.chat_id = requests.post("http://localhost:8080/api/bot/new-chat").json()["id"]
	st.session_state.first_message = requests.post("http://localhost:8080/api/bot/new-chat").json()["firstMessage"]
	st.write(st.session_state.chat_id)
	st.write(st.session_state.first_message)
	st.rerun()

if st.session_state.get("agent"):
	st.write("## Talk to Despair")
	
	# message history
	st.write(f"Despair: {st.session_state.first_message}")
	for message in st.session_state.messages:
		st.write(message)

	user_message = st.text_input("Message")
	
	if st.button("Send"):
	
		if user_message != "":
			st.session_state.messages.append(f"You: {user_message}")
			message = json.dumps({"id": st.session_state.chat_id, "content": user_message})
			
			response = requests.post("http://localhost:8080/api/bot/message", data=message,
									 headers={"Content-Type": "application/json"})
			st.session_state.messages.append(response.json()["content"])
			st.session_state.user_input = ""
		st.rerun()

if st.session_state.get("form"):
	frag_formComponent()
