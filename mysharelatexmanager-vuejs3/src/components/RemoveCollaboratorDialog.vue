<script setup>
import { ref, onMounted, watch, inject, onBeforeUnmount } from 'vue'
import { Modal } from 'bootstrap'
import GlobalService from '../js/GlobalService.js'

const errorState = inject('ErrorState')

const props = defineProps({
  visible: {
    type: Boolean,
    required: true,
  },
  user: {
    type: Object,
    required: true,
  },
})
const emit = defineEmits(['update:visible'])

const modalRef = ref()
let modal = null
const confirmEmail = ref('')

onMounted(() => {
  if (modalRef.value) {
    modal = new Modal(modalRef.value)
    modalRef.value.addEventListener('hidden.bs.modal', close)
  }
})

onBeforeUnmount(() => {
  modalRef.value?.removeEventListener('hidden.bs.modal', close)
  modal?.dispose()
})

watch(
  () => props.visible,
  (visible) => {
    if (!visible) {
      modal?.hide()
      return
    }
    confirmEmail.value = ''
    modal?.show()
  },
)

function onSuccessRemoveCollaborator() {
  close()
}

function onErrorRemoveCollaborator(error) {
  // TODO: display error on Toast
  console.log(error)
  errorState.methods.setErrorCode(error)
}

function removeCollaborator() {
  GlobalService.removeCollaborator(
    props.user.id,
    onSuccessRemoveCollaborator,
    onErrorRemoveCollaborator,
  )
}

function close() {
  emit('update:visible', false)
}
</script>

<template>
  <div
    id="removeCollaboratorModal"
    ref="modalRef"
    class="modal fade"
    tabindex="-1"
    aria-labelledby="exampleModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h1 id="exampleModalLabel" class="modal-title fs-5">Remove Collaborator Confirmation</h1>
          <button type="button" class="btn-close" aria-label="Close" @click="close"></button>
        </div>
        <div class="modal-body">
          <p class="my-4">
            You are about to <b>remove</b> collaborator {{ user.email }} from all associated
            projects. Are you absolutely sure?
          </p>
          <p class="my-4">Enter the following to confirm: {{ user.email }}</p>
          <input
            id="confirmEmail"
            v-model="confirmEmail"
            type="text"
            class="form-control"
            autocomplete="off"
          />
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="close()">Close</button>
          <button
            type="button"
            class="btn btn-primary"
            :disabled="confirmEmail !== user.email"
            @click="removeCollaborator"
          >
            Remove
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
