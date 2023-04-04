<script setup>
import { ref, onMounted, watch, reactive, inject } from 'vue'
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

const TransfertUserProjectsModeEnum = {
  Normal: 'NORMAL',
  Forced: 'FORCED',
  All: 'ALL'
}

const modalRef = ref()
const users = ref()
const ownerProjectCount = ref(0)
const confirmEmail = ref(null)

const transfertUserProjects = reactive({
  newOwnerId: '',
  currentOwnerId: '',
  mode: TransfertUserProjectsModeEnum.Normal
})

let modal = null
const transfertUserProjectsFeedbackMessage = ref('')

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
      transfertUserProjects.newOwnerId = ''
      transfertUserProjects.currentOwnerId = props.user.id
      transfertUserProjects.mode = TransfertUserProjectsModeEnum.Normal
      transfertUserProjectsFeedbackMessage.value = ''
      GlobalService.prepareTransfertUserProjects(
        props.user.id,
        onSuccessPrepareTransfertUserProjects,
        onErrorPrepareTransfertUserProjects
      )
    } else {
      modal.hide()
    }
  }
)

function applyTransfertUserProjects() {
  GlobalService.transfertUserProjects(
    transfertUserProjects,
    onSuccessTransfertUserProjects,
    onErrorTransfertUserProjects
  )
}

function onSuccessTransfertUserProjects(data) {
  if (data.ownerProject === undefined || data.ownerProject.length === 0) {
    close()
  } else {
    confirmEmail.value = ''
    ownerProjectCount.value = data.ownerProject.length
    transfertUserProjectsFeedbackMessage.value =
      props.user.email +
      ' still owns of ' +
      ownerProjectCount.value +
      ' project(s)'
  }
}

function onErrorTransfertUserProjects(error) {
  // TODO: display error on Toast
  console.log(error)
}

function onSuccessPrepareTransfertUserProjects(data) {
  // Emails
  users.value = data.data
    .map((user) => {
      return {
        id: user.id,
        email: user.email
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
  if (data.ownerProject === undefined) {
    ownerProjectCount.value = 0
  } else {
    ownerProjectCount.value = data.ownerProject.length
  }

  modal.show()
}

function onErrorPrepareTransfertUserProjects(error) {
  // TODO: display error on Toast
  console.log(error)
}

function close() {
  emit('update:visible', false)
}
</script>

<template>
  <div
    id="transfertOwnerProjectsDialog"
    ref="modalRef"
    class="modal fade"
    tabindex="-1"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5">Transfert Owner Projects</h1>
          <button
            type="button"
            class="btn-close"
            aria-label="Close"
            @click="close()"
          ></button>
        </div>
        <div class="modal-body">
          <form class="row">
            <div class="col-md-6 mb-3">
              <div class="mb-3">
                <label for="transfertCurrentOwner" class="form-label"
                  >Current Owner</label
                >
                <input
                  id="transfertCurrentOwner"
                  type="text"
                  :value="user.email"
                  placeholder="baron@ensma.fr"
                  disabled
                  class="form-control"
                />
              </div>
              <div class="mb-3">
                <label for="transfertOwnerProjects" class="form-label"
                  >Owner of</label
                >
                <input
                  id="transfertOwnerProjects"
                  type="text"
                  :value="ownerProjectCount"
                  disabled
                  class="form-control"
                />
              </div>
              <div class="mb-3">
                <label for="transfertNewOwner" class="form-label"
                  >New Owner</label
                >
                <select
                  id="transfertNewOwner"
                  v-model="transfertUserProjects.newOwnerId"
                  type="text"
                  placeholder="Id"
                  class="form-control"
                >
                  <option value="" disabled>Select your option</option>
                  <option
                    v-for="option in users"
                    :key="option.id"
                    :value="option.id"
                  >
                    {{ option.email }}
                  </option>
                </select>
              </div>
              <div class="mb-3">
                <label for="transfertNewOwner" class="form-label"
                  >Transfert Option</label
                >
                <div class="form-check">
                  <input
                    id="normalOption"
                    v-model="transfertUserProjects.mode"
                    class="form-check-input"
                    type="radio"
                    name="transfertNewOwner"
                    :value="TransfertUserProjectsModeEnum.Normal"
                    checked
                  />
                  <label class="form-check-label" for="normalOption"
                    >Normal</label
                  >
                </div>
                <div class="form-check">
                  <input
                    id="forcedOption"
                    v-model="transfertUserProjects.mode"
                    class="form-check-input"
                    type="radio"
                    name="transfertNewOwner"
                    :value="TransfertUserProjectsModeEnum.Forced"
                  />
                  <label class="form-check-label" for="forcedOption"
                    >Forced</label
                  >
                </div>
                <div class="form-check">
                  <input
                    id="allOption"
                    v-model="transfertUserProjects.mode"
                    class="form-check-input"
                    type="radio"
                    name="transfertNewOwner"
                    :value="TransfertUserProjectsModeEnum.All"
                  />
                  <label class="form-check-label" for="allOption">All</label>
                </div>
              </div>
            </div>
            <div class="col-md-6">
              <div class="card">
                <div class="card-body">
                  <h6 class="card-title">Notice for the transfert options</h6>
                  <ul class="list-group list-group-flush">
                    <li class="list-group-item">
                      <b>Normal</b>: transfert all projects to the new owner if
                      this new owner is a collaberator.
                    </li>
                    <li class="list-group-item">
                      <b>Forced</b>: transfert all projects if the projects are
                      shared (at least one collaberator).
                    </li>
                    <li class="list-group-item">
                      <b>All</b>: transfert all projects without any condition.
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <div class="mt-3">
              <p class="my-4">
                You are about to <b>transfert</b> projects of {{ user.email }},
                are you absolutely sure?
              </p>
              <p class="my-4">
                Enter the following to confirm: {{ user.email }}
              </p>
              <input
                id="inputInstitution"
                v-model="confirmEmail"
                type="text"
                class="form-control"
              />
            </div>
          </form>
          <div
            v-if="transfertUserProjectsFeedbackMessage !== ''"
            class="card border-dark mt-4"
          >
            <div class="card-header">Post transfert feedback</div>
            <div class="card-body">
              <p>{{ transfertUserProjectsFeedbackMessage }}</p>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="close">
            Close
          </button>
          <button
            type="button"
            class="btn btn-primary"
            :disabled="
              confirmEmail !== user.email ||
              transfertUserProjects.newOwnerId === ''
            "
            @click="applyTransfertUserProjects"
          >
            Transfert
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
