<script setup>
import { ref, onMounted, watch, inject } from 'vue'
import { Modal } from 'bootstrap'
import GlobalService from '../js/GlobalService.js'

const errorState = inject('ErrorState')

const props = defineProps({
  visible: {
    type: Boolean,
    required: true,
    default: false
  },
  user: {
    type: Object,
    required: true,
    default: () => ({})
  }
})
const emit = defineEmits(['update:visible'])

const modalRef = ref()
let modal = null
const confirmEmail = ref(null)

onMounted(() => {
  if (modalRef.value) {
    modal = new Modal(modalRef.value)
    modalRef.value.addEventListener('hidden.bs.modal', close)
  }
})

watch(
  () => props.visible,
  (visible) => {
    if (visible) {
      confirmEmail.value = ''
      modal.show()
    } else {
      modal.hide()
    }
  }
)

function onSuccessRemoveCollaberator() {
  close()
}

function onErrorRemoveCollaberator(error) {
  // TODO: display error on Toast
  console.log(error)
  errorState.methods.setErrorCode(error)
}

function applyRemoveCollaberator() {
  GlobalService.removeCollaberator(
    props.user.id,
    onSuccessRemoveCollaberator,
    onErrorRemoveCollaberator
  )
}

function close() {
  emit('update:visible', false)
}
</script>

<template>
  <div
    id="aboutPopupModal"
    ref="modalRef"
    class="modal fade"
    tabindex="-1"
    aria-labelledby="exampleModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h1 id="exampleModalLabel" class="modal-title fs-5">
            Remove Collaberator Confirmation
          </h1>
          <button
            type="button"
            class="btn-close"
            aria-label="Close"
            @click="close()"
          ></button>
        </div>
        <div class="modal-body">
          <p class="my-4">
            You are about to <b>remove</b> this collaberator
            {{ user.email }} from all projects related to him, are you
            absolutely sure?
          </p>
          <p class="my-4">Enter the following to confirm: {{ user.email }}</p>
          <input
            id="inputInstitution"
            v-model="confirmEmail"
            type="text"
            class="form-control"
          />
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="close()">
            Close
          </button>
          <button
            type="button"
            class="btn btn-primary"
            :disabled="confirmEmail !== user.email"
            @click="applyRemoveCollaberator"
          >
            Remove
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
