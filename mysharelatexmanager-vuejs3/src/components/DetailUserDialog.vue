<script setup>
import { ref, onMounted, watch, inject } from 'vue'
import CommonUtils from '../js/CommonUtils.js'
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
const emit = defineEmits(['update:visible', 'save-new-userdata'])

const modalRef = ref()
let modal = null
let newUser = {}

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
      newUser = { ...props.user }
      modal.show()
    } else {
      modal.hide()
    }
  }
)

function onSuccessUpdateUser(data) {
  if (data.status === 200) {
    emit('save-new-userdata', newUser)
  } else if (data.status === 204) {
    emit('save-new-userdata')
  }

  close()
}

function onErrorUpdateUser(error) {
  errorState.methods.setErrorCode(error)
}

function applyUpdateUser() {
  GlobalService.updateUser(newUser, onSuccessUpdateUser, onErrorUpdateUser)
}

function close() {
  emit('update:visible', false)
}
</script>

<template>
  <div ref="modalRef" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">User Edition</h5>
          <button
            type="button"
            class="btn-close"
            aria-label="Close"
            @click="close()"
          ></button>
        </div>
        <div class="modal-body">
          <form>
            <div class="mb-3">
              <label for="inputId" class="form-label">Id</label>
              <input
                id="inputId"
                :value="newUser.id"
                type="text"
                placeholder="Id"
                disabled
                class="form-control"
              />
            </div>
            <div class="mb-3">
              <label for="inputFirstname" class="form-label">First name</label>
              <input
                id="inputFirstname"
                v-model="newUser.first_name"
                type="text"
                class="form-control"
                placeholder="First name"
                @keydown.enter="applyUpdateUser"
              />
            </div>
            <div class="mb-3">
              <label for="inputLastname" class="form-label">Last name</label>
              <input
                id="inputLastname"
                v-model="newUser.last_name"
                type="text"
                class="form-control"
                placeholder="Last name"
                @keydown.enter="applyUpdateUser"
              />
            </div>
            <div class="mb-3">
              <label for="inputInstitution" class="form-label"
                >Institution</label
              >
              <input
                id="inputInstitution"
                v-model="newUser.institution"
                type="text"
                class="form-control"
                placeholder="Institution"
                @keydown.enter="applyUpdateUser"
              />
            </div>
            <div class="mb-3">
              <label for="inputEmail" class="form-label">Email</label>
              <input
                id="inputEmail"
                v-model="newUser.email"
                type="email"
                class="form-control"
                placeholder="user@domain.fr"
                @keydown.enter="applyUpdateUser"
              />
            </div>
            <div class="mb-5">
              <label for="inputPassword" class="form-label">Password</label>
              <input
                id="inputPassword"
                v-model="newUser.hashedPassword"
                placeholder="If empty password, it will be not changed."
                type="password"
                class="form-control"
                @keydown.enter="applyUpdateUser"
              />
            </div>
            <div class="mb-3">
              <label for="inputLoginCount" class="form-label"
                >Login Count</label
              >
              <input
                id="inputLoginCount"
                :value="newUser.loginCount"
                disabled
                type="text"
                class="form-control"
                placeholder="Ex: 125"
              />
            </div>
            <div class="mb-5">
              <label for="inputLastLoggedIn" class="col-form-label"
                >Last Logged In</label
              >
              <input
                id="inputLastLoggedIn"
                :value="CommonUtils.displayDate(newUser.lastLoggedIn)"
                disabled
                type="text"
                class="form-control"
                placeholder="No Connection"
              />
            </div>
            <div class="mb-3">
              <div class="form-check">
                <input
                  id="checkIsAdmin"
                  v-model="newUser.isAdmin"
                  class="form-check-input"
                  type="checkbox"
                />
                <label class="form-check-label" for="checkIsAdmin">
                  Is admin ?
                </label>
              </div>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="close()">
            Close
          </button>
          <button
            type="button"
            class="btn btn-primary"
            @click="applyUpdateUser()"
          >
            Save changes
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
