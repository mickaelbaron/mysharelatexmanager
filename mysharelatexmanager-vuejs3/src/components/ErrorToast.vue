<script setup>
import { inject, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { Toast } from 'bootstrap'
import { useRouter } from 'vue-router'

const router = useRouter()
const errorState = inject('ErrorState')

const toastRef = ref(null)
const errorCode = ref(200)
const errorMessage = ref('')

let toast

function onToastHidden() {
  if (errorCode.value === 401) {
    router.push({ name: 'SignIn' })
  }
}

onMounted(() => {
  if (!toastRef.value) return

  toast = new Toast(toastRef.value)
  toastRef.value.addEventListener('hidden.bs.toast', onToastHidden)
})

onBeforeUnmount(() => {
  toastRef.value?.removeEventListener('hidden.bs.toast', onToastHidden)
})

watch(
  () => errorState.state.code,
  (code) => {
    if (code === 200) return

    errorCode.value = code

    switch (code) {
      case 401:
        errorMessage.value = 'Invalid username or password.'
        break

      default:
        errorMessage.value = errorState.state.message
    }

    errorState.methods.setError(200)
    toast?.show()
  },
)
</script>

<template>
  <div class="toast-container position-fixed top-0 end-0 p-3">
    <div
      ref="toastRef"
      class="toast"
      role="alert"
      aria-live="assertive"
      aria-atomic="true"
      data-bs-animation="true"
      data-bs-autohide="true"
      data-bs-delay="5000"
    >
      <div class="toast-header">
        <strong class="me-auto">MyShareLaTexManager</strong>
        <small>Error message</small>

        <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close" />
      </div>

      <div class="toast-body">
        {{ errorMessage }}
      </div>
    </div>
  </div>
</template>
