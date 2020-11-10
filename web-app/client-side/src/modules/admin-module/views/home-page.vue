<template>
    <div>   
        <TheNavbar />
        <v-app id="inspire" >
			<v-container>
				<TableComponent />
			</v-container>
		</v-app>
        
            
    </div>

</template>

<script>
    import TheNavbar from '../../core-module/components/the-nav-bar'
    import TableComponent from '../../core-module/components/tabel-component'
    import http from '../../core-module/services/http'

    export default {
        name: 'HomePage',
        components: {
            TheNavbar,
            TableComponent
        },
        data(){
            return {
            }
        },
        methods: {
            getHumanTask: function() {
                http.get('workflow/human/tasks').then(response=> {
                    console.log(response)
                });
            }
        },
        mounted: function(){
            this.getHumanTask();
        }
    }
</script>