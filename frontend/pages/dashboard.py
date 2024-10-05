import streamlit as st

st.image('logo.png')

st.metric('Version', '0.4.1')

# check auth
if st.session_state["authentication_status"]:
    st.write('___')
    authenticator.logout()
    st.write(f'Welcome *{st.session_state["name"]}*')
    st.title('Some content')    
    st.write('___')
elif st.session_state["authentication_status"] is False:
    st.switch_page('home.py')