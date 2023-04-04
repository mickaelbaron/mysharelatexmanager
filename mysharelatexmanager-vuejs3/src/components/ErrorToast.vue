<script setup>
import { reactive, ref, onMounted, watch, inject } from 'vue'
import { Toast } from 'bootstrap'
import { useRouter } from 'vue-router'

const errorState = inject('ErrorState')
const router = useRouter()

const myState = reactive({
  code: 200,
  message: ''
})

const modalRef = ref()
let toast = null

onMounted(() => {
  if (modalRef.value) {
    toast = new Toast(modalRef.value)
    modalRef.value.addEventListener('hidden.bs.toast', close)
  }
})

watch(errorState.state, () => {
  // Do nothing, clear error.
  if (errorState.state.code === 200) {
    return
  }

  myState.code = errorState.state.code

  if (myState.code === 401) {
    myState.message = 'Invalid username or password.'
  } else {
    myState.message = errorState.state.message
  }

  // Initialize errorState.
  errorState.methods.setError(200)
  toast.show()
})

function close() {
  if (myState.code === 401) {
    router.push({
      name: 'SignIn'
    })
  }
}
</script>

<template>
  <div class="toast-container position-fixed top-0 end-0 p-3">
    <div
      id="liveToast"
      ref="modalRef"
      class="toast"
      role="alert"
      aria-live="assertive"
      data-bs-animation="true"
      data-bs-autohide="true"
      data-bs-delay="5000"
      aria-atomic="true"
    >
      <div class="toast-header">
        <strong class="me-auto">MyShareLaTexManager</strong>
        <small>Error message</small>
        <button
          type="button"
          class="btn-close"
          data-bs-dismiss="toast"
          aria-label="Close"
        ></button>
      </div>
      <div class="toast-body">{{ myState.message }}</div>
    </div>
  </div>
</template>
