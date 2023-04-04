<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'

import GlobalService from '../js/GlobalService.js'

const version = ref(import.meta.env.PACKAGE_VERSION)
const router = useRouter()

const errorMessage = ref('')
const credentials = reactive({
  username: '',
  password: ''
})

const isValid = computed(() => {
  return credentials.username !== '' && credentials.password !== ''
})

function credentialsChange() {
  errorMessage.value = ''
}

function onSuccessSignIn(data) {
  localStorage.setItem('TOKEN', data.token)
  router.push({
    name: 'UsersEditor'
  })
}

function onErrorSignIn(error) {
  if (error === 401) {
    errorMessage.value = 'Invalid username or password.'
  } else {
    errorMessage.value =
      'No connection to the server, please contact the administrator.'
  }
}

function onSubmit() {
  GlobalService.getAuthenticationLogin(
    credentials,
    onSuccessSignIn,
    onErrorSignIn
  )
}
</script>

<template>
  <div class="container">
    <div class="wrapper">
      <form name="Login_Form" class="form-signin" @submit.prevent="onSubmit">
        <h3 class="form-signin-heading">MyShareLatexManager V{{ version }}</h3>
        <hr />
        <div class="form-floating mt-4">
          <input
            id="floatingInput"
            v-model="credentials.username"
            type="text"
            class="form-control"
            placeholder="name@example.com"
            @change="credentialsChange"
          />
          <label for="floatingInput">Username</label>
        </div>
        <div class="form-floating mb-4">
          <input
            id="floatingPassword"
            v-model="credentials.password"
            type="password"
            class="form-control"
            placeholder="Password"
            @change="credentialsChange"
          />
          <label for="floatingPassword">Password</label>
        </div>

        <button
          :disabled="!isValid"
          class="btn btn-lg btn-primary w-100"
          type="submit"
        >
          Sign In
        </button>
        <div v-if="errorMessage !== ''">
          <div class="text-center alert alert-danger mt-4" role="alert">
            {{ errorMessage }}
          </div>
        </div>
      </form>
    </div>
    <div class="alert alert-warning" role="alert">
      <b>Disclaimer</b>: MyShareLatexManager is a personnal tool hosted to my
      Github account. There is no affiliation with the company that publishes
      Overleaf/Sharelatex. Any issues related to the MyShareLatexManager tool
      should be reported on the MyShareLatexManager repository :
      <a
        target="_blank"
        href="https://github.com/mickaelbaron/mysharelatexmanager"
        >github.com/mickaelbaron/mysharelatexmanager</a
      >
    </div>
  </div>
</template>

<style>
h3 {
  font-size: 1.2rem !important;
}

.wrapper {
  margin-top: 180px;
  margin-bottom: 20px;
}

.form-signin {
  max-width: 450px;
  padding: 50px 25px 25px;
  margin: 0 auto;
  background-color: #eee;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.form-signin-heading {
  text-align: center;
  margin-bottom: 30px;
}
</style>
