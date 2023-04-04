import { reactive, readonly } from 'vue'

const state = reactive({
  code: 200,
  message: ''
})

const methods = {
  setError(newCode, newMessage) {
    state.code = newCode
    state.message = newMessage
  },

  setErrorCode(newCode) {
    state.code = newCode
  }
}

export default {
  state: readonly(state),
  methods
}
