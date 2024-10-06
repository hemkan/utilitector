import json

import requests
import streamlit as st

# initialize session state things
for (state_name, default) in (
# 		('form', False),
# 		('agent', False),
# 		('messages', []),
# 		('loc_country', None),
# 		('first_message', "Hello, how can I help you today?"),
# 		('chat_id', None)

        ('form', False),
        ('agent', False),
        ('messages', []),
        ('loc_country', None)
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
		
# <<<<<<< frontend
# 		try:
# 			response = requests.post("http://localhost:8080/api/report/submit", data=report_json, headers={"Content-Type": "application/json"})
# 			st.success("Report submitted")
# 		except:
# 			st.error("Failed to submit report")
# =======
		# TODO: endpoint w report_json
		# response = requests.post("http://localhost:8080/api/report/submit", data=report_json,
		# 						 headers={"Content-Type": "application/json"})
		st.write(response)
	
	st.button("Submit", on_click=onSubmit)

if 'usermessage' not in st.session_state:
    st.session_state.usermessage = ""

def submit():
    st.session_state.usermessage = st.session_state.widget
    st.session_state.widget = ""
# >>>>>>> main


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

# <<<<<<< frontend
# st.write("# Report")
# st.write("**If you are in immediate danger, please call 911.**")
# st.write("*Please select an option below to file a report or get help from an agent.*")

# left, right = st.columns(2)
# if left.button("File a Report Manually", use_container_width=True):
# 	st.session_state.form = True
# 	st.session_state.agent = False
# 	st.rerun()

# if right.button("Help from an Agent", use_container_width=True):
# 	st.session_state.agent = True
# 	st.session_state.form = False
# 	st.session_state.chat_id = requests.post("http://localhost:8080/api/bot/new-chat").json()["id"]
# 	st.session_state.first_message = requests.post("http://localhost:8080/api/bot/new-chat").json()["firstMessage"]
# 	st.write(st.session_state.chat_id)
# 	st.write(st.session_state.first_message)
# 	st.rerun()

# if st.session_state.get("agent"):
# 	st.write("## Talk to Despair")
	
# 	# message history
# 	st.write(f"Despair: {st.session_state.first_message}")
# 	for message in st.session_state.messages:
# 		st.write(message)

# 	user_message = st.text_input("Message")
	
# 	if st.button("Send"):
	
# 		if user_message != "":
# 			st.session_state.messages.append(f"You: {user_message}")
# 			message = json.dumps({"id": st.session_state.chat_id, "content": user_message})
			
# 			response = requests.post("http://localhost:8080/api/bot/message", data=message,
# 									 headers={"Content-Type": "application/json"})
# 			st.session_state.messages.append(response.json()["content"])
# 			st.session_state.user_input = ""
# 		st.rerun()

# if st.session_state.get("form"):
# 	frag_formComponent()
# =======
if 'chat_id' not in st.session_state:
    st.session_state.chat_id = None

if 'messages' not in st.session_state:
    st.session_state.messages = []

if st.button("File a Report Manually"):
    st.session_state.form = True
    st.session_state.agent = False

if st.button("Help from an Agent") and not st.session_state.agent:
    st.session_state.agent = True
    st.session_state.form = False
    
    # TODO: endpoint for get a chat_id
    st.session_state.chat_id = requests.post("http://localhost:8080/api/bot/new-chat").json()["id"]
    firstmessage = requests.post("http://localhost:8080/api/bot/new-chat").json()["firstMessage"]
    st.session_state.messages = []
    st.session_state.messages.append(f"DaddyDiz: {firstmessage}")
    # chat_id = requests.post("http://localhost:8080/bot/new-chat").json()["id"]
    #st.write(st.session_state.chat_id)

if st.session_state.get("agent"):
    st.write("# Talk to DaddyDiz")
    
    # message history
    for message in st.session_state.messages:
        st.write(message)
    
    st.text_input("Message", key="widget", on_change=submit, placeholder="Type in a message and press ENTER")
    if st.session_state.usermessage:
        if st.session_state.usermessage != "":
            st.session_state.messages.append(f"\tYou: {st.session_state.usermessage}")
            message = json.dumps({"id": st.session_state.chat_id, "content": st.session_state.usermessage})
            response = requests.post("http://localhost:8080/api/bot/message", data=message,
                                     headers={"Content-Type": "application/json"})
            st.session_state.usermessage = ""
            botmessage = response.json()["content"]
            st.session_state.messages.append(f"DaddyDiz: {botmessage}")
            st.rerun()

if st.session_state.get("form"):
    frag_formComponent()


# >>>>>>> main
