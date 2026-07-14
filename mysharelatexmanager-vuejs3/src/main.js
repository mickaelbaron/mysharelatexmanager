import { createApp } from 'vue'
import App from './App.vue'
import PrimeVue from 'primevue/config'

import router from './router'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-icons/font/bootstrap-icons.css'
import 'bootstrap'

import 'primevue/resources/themes/saga-blue/theme.css'
import 'primevue/resources/primevue.min.css'
import 'primeicons/primeicons.css'
import  './js/RegisterFilters.js'

const app = createApp(App)

app.use(router)
app.use(PrimeVue)

app.mount('#app')
