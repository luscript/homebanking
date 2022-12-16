const { createApp } = Vue;

createApp({
    data() {
        return {
            client: {},
            accounts: [],
            checked: undefined
        }
    },
    created() {
        this.checked = JSON.parse(localStorage.getItem("checked"))
        axios.get('http://localhost:8080/api/clients/1')
            .then(data => {
                this.client = data.data;
                this.accounts = data.data.accounts;
                console.log(this.client)
            })
        .catch(err => console.log(err))
    },
    methods: {
        
    },
    computed: {
        saveOnLocalStorage() {
            localStorage.setItem("checked", JSON.stringify(this.checked)); 
            console.log(this.checked)
        }
    }
        
}).mount('#app')