<script setup>
import { ref, reactive, onMounted, computed, onBeforeUnmount, watch, inject, toRaw } from 'vue'
import CommonUtils from '../js/CommonUtils.js'
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

const emit = defineEmits({
  'update:visible': null,
  'save-new-userdata': null,
})

const modalRef = ref()
let modal = null
const newUser = reactive({
  id: null,
  first_name: '',
  last_name: '',
  institution: '',
  email: '',
  hashed_password: '',
  login_count: 0,
  last_logged_in: null,
  admin: false,
})

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

const formattedLastLogin = computed(() => CommonUtils.displayDate(newUser.last_logged_in))

watch(
  () => props.visible,
  (visible) => {
    if (!modal) return

    if (!visible) {
      modal.hide()
      return
    }

    Object.assign(newUser, structuredClone(toRaw(props.user)))
    modal.show()
  },
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
      <form @submit.prevent="applyUpdateUser">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">User Edition</h5>
            <button type="button" class="btn-close" aria-label="Close" @click="close()"></button>
          </div>
          <div class="modal-body">
            <div class="mb-3">
              <label for="inputId" class="form-label">Id</label>
              <input
                id="inputId"
                v-model="newUser.id"
                type="text"
                placeholder="Id"
                readonly
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
              />
            </div>
            <div class="mb-3">
              <label for="inputInstitution" class="form-label">Institution</label>
              <input
                id="inputInstitution"
                v-model="newUser.institution"
                type="text"
                class="form-control"
                placeholder="Institution"
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
              />
            </div>
            <div class="mb-5">
              <label for="inputPassword" class="form-label">Password</label>
              <input
                id="inputPassword"
                v-model="newUser.hashed_password"
                placeholder="If empty password, it will be not changed."
                type="password"
                class="form-control"
              />
            </div>
            <div class="mb-3">
              <label for="inputLoginCount" class="form-label">Login Count</label>
              <input
                id="inputLoginCount"
                v-model="newUser.login_count"
                readonly
                disabled
                type="text"
                class="form-control"
                placeholder="Ex: 125"
              />
            </div>
            <div class="mb-5">
              <label for="inputLastLoggedIn" class="col-form-label">Last Logged In</label>
              <input
                id="inputLastLoggedIn"
                :value="formattedLastLogin"
                readonly
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
                  v-model="newUser.admin"
                  class="form-check-input"
                  type="checkbox"
                />
                <label class="form-check-label" for="checkIsAdmin"> Administrator </label>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" @click="close()">Close</button>
            <button type="submit" class="btn btn-primary" @click="applyUpdateUser()">
              Save changes
            </button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>
