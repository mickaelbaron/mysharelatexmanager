<script setup>
import { ref, onMounted, onBeforeUnmount, watch, reactive, inject } from 'vue'
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

const TransferUserProjectsModeEnum = Object.freeze({
  Normal: 'NORMAL',
  Forced: 'FORCED',
  All: 'ALL',
})

const modalRef = ref()
const users = ref([])
const ownerProjectCount = ref(0)
const confirmEmail = ref(null)

const transferUserProjects = reactive({
  new_owner_ref: '',
  current_owner_ref: '',
  mode: TransferUserProjectsModeEnum.Normal,
})

let modal = null
const feedbackMessage = ref('')

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
    if (visible) {
      resetForm()
      GlobalService.prepareTransferUserProjects(
        props.user.id,
        onSuccessPrepareTransferUserProjects,
        onErrorPrepareTransferUserProjects,
      )
    } else {
      modal?.hide()
    }
  },
)

function resetForm() {
  confirmEmail.value = ''
  transferUserProjects.new_owner_ref = ''
  transferUserProjects.current_owner_ref = props.user.id
  transferUserProjects.mode = TransferUserProjectsModeEnum.Normal
  feedbackMessage.value = ''
}

function applyTransferUserProjects() {
  GlobalService.transferUserProjects(
    transferUserProjects,
    onSuccessTransferUserProjects,
    onErrorTransferUserProjects,
  )
}

function onSuccessTransferUserProjects(data) {
  if (data.owned_projects === undefined || data.owned_projects.length === 0) {
    close()
  } else {
    confirmEmail.value = ''
    ownerProjectCount.value = data.owned_projects.length
    feedbackMessage.value = `${props.user.email} still owns ${ownerProjectCount.value} project(s).`
  }
}

function onErrorTransferUserProjects(error) {
  // TODO: display error on Toast
  console.log(error)
}

function onSuccessPrepareTransferUserProjects(data) {
  // Emails
  users.value = data.data
    .map((user) => {
      return {
        id: user.id,
        email: user.email,
      }
    })
    .filter((user) => user.id !== props.user.id)
    .sort((a, b) => {
      if (a.email > b.email) {
        return 1
      }
      if (a.email < b.email) {
        return -1
      }
      return 0
    })

  // Number of projects
  if (data.owned_projects === undefined) {
    ownerProjectCount.value = 0
  } else {
    ownerProjectCount.value = data.owned_projects.length
  }

  modal.show()
}

function onErrorPrepareTransferUserProjects(error) {
  // TODO: display error on Toast
  console.log(error)
}

function close() {
  emit('update:visible', false)
}
</script>

<template>
  <div
    id="transferOwnerProjectsDialog"
    ref="modalRef"
    class="modal fade"
    tabindex="-1"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5">Transfer Owner Projects</h1>
          <button type="button" class="btn-close" aria-label="Close" @click="close()"></button>
        </div>
        <div class="modal-body">
          <form class="row">
            <div class="col-md-6 mb-3">
              <div class="mb-3">
                <label for="transferCurrentOwner" class="form-label">Current Owner</label>
                <input
                  id="transferCurrentOwner"
                  type="text"
                  :value="user.email"
                  placeholder="baron@ensma.fr"
                  disabled
                  class="form-control"
                />
              </div>
              <div class="mb-3">
                <label for="transferOwnerProjects" class="form-label">Owner of</label>
                <input
                  id="transferOwnerProjects"
                  type="text"
                  :value="ownerProjectCount"
                  disabled
                  class="form-control"
                />
              </div>
              <div class="mb-3">
                <label for="transferNewOwner" class="form-label">New Owner</label>
                <select
                  id="transferNewOwner"
                  v-model="transferUserProjects.new_owner_ref"
                  placeholder="Id"
                  class="form-control"
                >
                  <option value="" disabled>Select your option</option>
                  <option v-for="option in users" :key="option.id" :value="option.id">
                    {{ option.email }}
                  </option>
                </select>
              </div>
              <div class="mb-3">
                <label for="transferNewOwner" class="form-label">Transfer Option</label>
                <div class="form-check">
                  <input
                    id="normalOption"
                    v-model="transferUserProjects.mode"
                    class="form-check-input"
                    type="radio"
                    name="transferNewOwner"
                    :value="TransferUserProjectsModeEnum.Normal"
                    checked
                  />
                  <label class="form-check-label" for="normalOption">Normal</label>
                </div>
                <div class="form-check">
                  <input
                    id="forcedOption"
                    v-model="transferUserProjects.mode"
                    class="form-check-input"
                    type="radio"
                    name="transferNewOwner"
                    :value="TransferUserProjectsModeEnum.Forced"
                  />
                  <label class="form-check-label" for="forcedOption">Forced</label>
                </div>
                <div class="form-check">
                  <input
                    id="allOption"
                    v-model="transferUserProjects.mode"
                    class="form-check-input"
                    type="radio"
                    name="transferNewOwner"
                    :value="TransferUserProjectsModeEnum.All"
                  />
                  <label class="form-check-label" for="allOption">All</label>
                </div>
              </div>
            </div>
            <div class="col-md-6">
              <div class="card">
                <div class="card-body">
                  <h6 class="card-title">Notice for the transfer options</h6>
                  <ul class="list-group list-group-flush">
                    <li class="list-group-item">
                      <b>Normal</b>: transfer all projects to the new owner if this new owner is a
                      collaborator.
                    </li>
                    <li class="list-group-item">
                      <b>Forced</b>: transfer all projects if the projects are shared (at least one
                      collaborator).
                    </li>
                    <li class="list-group-item">
                      <b>All</b>: transfer all projects without any condition.
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <div class="mt-3">
              <p class="my-4">
                You are about to <b>transfer</b> projects of {{ user.email }}, are you absolutely
                sure?
              </p>
              <p class="my-4">Enter the following to confirm: {{ user.email }}</p>
              <input id="confirmEmail" v-model="confirmEmail" type="text" class="form-control" />
            </div>
          </form>
          <div v-if="feedbackMessage !== ''" class="card border-dark mt-4">
            <div class="card-header">Post transfer feedback</div>
            <div class="card-body">
              <p>{{ feedbackMessage }}</p>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="close">Close</button>
          <button
            type="button"
            class="btn btn-primary"
            :disabled="confirmEmail !== user.email || transferUserProjects.new_owner_ref === ''"
            @click="applyTransferUserProjects"
          >
            Transfer
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
