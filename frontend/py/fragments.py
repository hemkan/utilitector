import pycountry
import streamlit as st
from geopy import Nominatim, Location
from geopy.extra.rate_limiter import RateLimiter


@st.fragment
def locationForm():
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
