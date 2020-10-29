<template>
	<div id="app">
		<v-app id="inspire" >
			<v-container>
				<router-view/>
			</v-container>
		</v-app>
	</div>
</template>

<style lang="scss">

	@import url(http://fonts.googleapis.com/earlyaccess/amiri.css);

	.v-application {
		background-color:#f2f2f2!important;
		font-family:  'Amiri', serif !important;
		.title { // To pin point specific classes of some components
			font-family: 'Amiri', serif !important;
		}
	}
	.theme--light.v-label {
		color:rgba(0,0,0,.6)
	}
	.theme--light.v-application {
		background-color: #f2f2f2;
	}
	h1 {
		font-size: 26px;
	}
</style>
<script>

export default {
	components: {
	},
}
</script>