import streamlit as st

if 'form' not in st.session_state:
    st.session_state.form = False

if 'agent' not in st.session_state:
    st.session_state.agent = False

if 'messages' not in st.session_state:
    st.session_state.messages = []

st.write("# Report")

if st.button("File a Report Manually"):
    st.session_state.form = True
    st.session_state.agent = False

if st.button("Help from an Agent"):
    st.session_state.agent = True
    st.session_state.form = False

if st.session_state.get("agent"):
    st.write("# Talk to Despair")
    st.write("Despair: Hello there! I'm Despair. How can I help you today?")
    for message in st.session_state.messages:
        st.write(message)

    user_message = st.text_input("Message")

    if st.button("Send"):
        if user_message:
            st.session_state.messages.append(f"You: {user_message}")
            st.session_state.messages.append(f"Despair: Thank you for your message!")

if st.session_state.get("form"):
    st.write("# File a Report")

    name = st.text_input("Name")
    address = st.text_input("Address")
    type = st.selectbox("Type", ["Electricity", "Water", "Gas"])
    description = st.text_area("Description")
    
    if st.button("Submit"):
        st.write("Report submitted successfully")
        st.write(f"Name: {name}")
        st.write(f"Address: {address}")
        st.write(f"Type: {type}")
        st.write(f"Description: {description}")