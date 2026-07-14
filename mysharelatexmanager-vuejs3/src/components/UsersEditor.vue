<script setup>
import { ref, onMounted, reactive, watch, inject } from 'vue'
import { FilterService, FilterMatchMode } from 'primevue/api'

import DataTable from 'primevue/datatable'
import Column from 'primevue/column'

import DetailUserRow from './DetailUserRow.vue'
import DetailUserDialog from './DetailUserDialog.vue'
import DeleteUserDialog from './DeleteUserDialog.vue'
import TransferOwnerProjectsDialog from './TransferOwnerProjectsDialog.vue'
import RemoveCollaboratorDialog from './RemoveCollaboratorDialog.vue'

import GlobalService from '../js/GlobalService.js'
import CommonUtils from '../js/CommonUtils.js'
import { DATE_FILTER } from '../js/RegisterFilters.js'

const errorState = inject('ErrorState')

const props = defineProps({
  searchContent: {
    type: String,
    required: true,
  },
})

const allUsers = ref([])

const loading = ref(true)
const expandedRows = ref([])

const dialogs = reactive({
  detail: false,
  delete: false,
  transfer: false,
  remove: false,
})

const currentUserEdit = reactive({
  id: '',
  first_name: '',
  last_name: '',
  institution: '',
  email: '',
  login_count: '',
  last_logged_in: '',
  hashed_password: '',
  admin: true,
})

const createFilter = (matchMode) => ({
  value: null,
  matchMode,
})

const globalFilter = ref({
  global: createFilter(FilterMatchMode.CONTAINS),
  id: createFilter(FilterMatchMode.STARTS_WITH),
  first_name: createFilter(FilterMatchMode.STARTS_WITH),
  last_name: createFilter(FilterMatchMode.STARTS_WITH),
  email: createFilter(FilterMatchMode.STARTS_WITH),
  login_count: createFilter(FilterMatchMode.EQUALS),
  last_logged_in: createFilter(DATE_FILTER),
})

function onSuccessUsers(data) {
  allUsers.value = data.data
  loading.value = false
}

function onErrorUsers(error) {
  errorState.methods.setErrorCode(error)
}

watch(
  () => props.searchContent,
  (searchContent) => {
    globalFilter.value.global.value = searchContent
  },
)

onMounted(() => {
  loading.value = true
  GlobalService.getUsers(onSuccessUsers, onErrorUsers)
})

function saveNewUserData(data) {
  if (data) {
    const objectToReplace = allUsers.value.find((currentUser) => currentUser.id === data.id)

    if (objectToReplace) {
      Object.assign(objectToReplace, data)
    }
  }
}

function selectCurrentUserEdit(user) {
  Object.assign(currentUserEdit, user)
}

function openDialog(user, dialogName) {
  selectCurrentUserEdit(user)
  dialogs[dialogName] = true
}
</script>

<template>
  <RemoveCollaboratorDialog v-model:visible="dialogs.remove" :user="currentUserEdit" />

  <DeleteUserDialog v-model:visible="dialogs.delete" :user="currentUserEdit" />

  <DetailUserDialog
    v-model:visible="dialogs.detail"
    :user="currentUserEdit"
    @save-new-userdata="saveNewUserData"
  />

  <TransferOwnerProjectsDialog v-model:visible="dialogs.transfer" :user="currentUserEdit" />

  <DataTable
    v-model:expandedRows="expandedRows"
    v-model:filters="globalFilter"
    filter-display="row"
    :paginator="true"
    :rows="100"
    :rows-per-page-options="[10, 20, 50, 100]"
    :value="allUsers"
    data-key="id"
    current-page-report-template="Showing {first} to {last} of {totalRecords}"
    paginator-template="CurrentPageReport FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown"
    :global-filter-fields="['id', 'first_name', 'last_name', 'email']"
    :loading="loading"
  >
    <template #loading> Loading customers data. Please wait. </template>
    <Column :expander="true" header-style="width: 3rem" />
    <Column field="id" :sortable="true" header="Id">
      <template #filter="{ filterModel, filterCallback }">
        <input
          v-model="filterModel.value"
          type="text"
          class="form-control"
          :placeholder="`Search by Id - ${filterModel.matchMode}`"
          aria-label="id"
          aria-describedby="basic-addon1"
          @change="filterCallback()"
        />
      </template>
    </Column>
    <Column field="first_name" :sortable="true" header="First Name">
      <template #filter="{ filterModel, filterCallback }">
        <input
          v-model="filterModel.value"
          type="text"
          class="form-control"
          :placeholder="`Search by First name - ${filterModel.matchMode}`"
          aria-label="first name"
          aria-describedby="basic-addon1"
          @change="filterCallback()"
        />
      </template>
    </Column>
    <Column field="last_name" :sortable="true" header="Last Name">
      <template #filter="{ filterModel, filterCallback }">
        <input
          v-model="filterModel.value"
          type="text"
          class="form-control"
          :placeholder="`Search by Last name - ${filterModel.matchMode}`"
          aria-label="last name"
          aria-describedby="basic-addon1"
          @change="filterCallback()"
        />
      </template>
    </Column>
    <Column field="email" header="Email" :sortable="true">
      <template #filter="{ filterModel, filterCallback }">
        <input
          v-model="filterModel.value"
          type="text"
          class="form-control"
          :placeholder="`Search by Email - ${filterModel.matchMode}`"
          aria-label="email"
          aria-describedby="basic-addon1"
          @change="filterCallback()"
        />
      </template>
    </Column>
    <Column field="login_count" header="Login Count" data-type="numeric" :sortable="true">
      <template #filter="{ filterModel, filterCallback }">
        <input
          v-model="filterModel.value"
          type="text"
          class="form-control"
          placeholder="Numeric"
          aria-label="loginCount"
          aria-describedby="basic-addon1"
          @change="filterCallback()"
        />
      </template>
    </Column>
    <Column
      field="last_logged_in"
      header="Last Logged In"
      :sortable="true"
      :show-filter-menu="false"
      ><template #body="slotProps">{{
        CommonUtils.displayDate(slotProps.data.last_logged_in)
      }}</template>
      <template #filter="{ filterModel, filterCallback }">
        <input
          v-model="filterModel.value"
          type="text"
          class="form-control"
          placeholder="YYYY/MM/D HH:mm:ss"
          aria-label="lastUpdated"
          aria-describedby="basic-addon1"
          @change="filterCallback()"
        />
      </template>
    </Column>
    <Column style="min-width: 8rem">
      <template #body="slotProps">
        <div class="d-flex flex-nowrap">
          <button
            type="button"
            class="btn btn-outline-primary"
            :title="'Edit the content of ' + slotProps.data.email"
            @click="openDialog(slotProps.data, 'detail')"
          >
            <i class="bi bi-pencil"></i>
          </button>
          <button
            type="button"
            class="btn btn-outline-primary ms-2"
            :title="'Transfer the projects of ' + slotProps.data.email + ' to another user'"
            @click="openDialog(slotProps.data, 'transfer')"
          >
            <i class="bi bi-shuffle"></i>
          </button>
          <button
            type="button"
            class="btn btn-outline-danger ms-2"
            :title="'Remove collaborator (' + slotProps.data.email + ') of all projects'"
            @click="openDialog(slotProps.data, 'remove')"
          >
            <i class="bi bi-person-fill-dash"></i>
          </button>
          <!--<button
            type="button"
            class="btn btn-outline-danger ms-2"
            :title="'Delete ' + slotProps.data.email"
            @click="openDeleteUserDialog(slotProps.data)"
          >
            <i class="bi bi-trash"></i>
          </button>-->
        </div>
      </template>
    </Column>
    <template #expansion="slotProps">
      <DetailUserRow :data="slotProps.data" />
    </template>
  </DataTable>
</template>
