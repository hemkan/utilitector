import streamlit as st

# Initialize session state variables for form and agent
if 'form' not in st.session_state:
    st.session_state.form = False

if 'agent' not in st.session_state:
    st.session_state.agent = False

if 'messages' not in st.session_state:
    st.session_state.messages = []

if st.button("File a Report"):
    st.session_state.form = True
    st.session_state.agent = False

if st.button("Talk to an Agent"):
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
    description = st.text_area("Description")
    
    if st.button("Submit"):
        st.write("Report submitted successfully")
        st.write(f"Name: {name}")
        st.write(f"Address: {address}")
        st.write(f"Description: {description}")