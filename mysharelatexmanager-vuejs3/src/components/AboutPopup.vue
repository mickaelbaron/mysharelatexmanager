<script setup>
import { ref, onMounted, watch } from 'vue'
import { Modal } from 'bootstrap'

const version = ref(import.meta.env.PACKAGE_VERSION)

const props = defineProps({
  visible: {
    type: Boolean,
    required: true,
    default: false
  }
})
const emit = defineEmits(['update:visible'])

const modalRef = ref()
let modal = null

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
      modal.show()
    } else {
      modal.hide()
    }
  }
)

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
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h1 id="exampleModalLabel" class="modal-title fs-5">
            About MyShareLaTexManager tool V{{ version }}
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
            MyShareLaTexManager is a <b>UI tool</b> to manage
            <b>users and projects</b> for ShareLaTex/Overleaf
            <b>self hosted</b> instances (Community and Server Pro versions).
          </p>
          <p class="my-4">
            Developped by
            <a href="https://mickael-baron.fr" target="_blank">Mickael BARON</a>
            (Twitter:
            <a href="https://twitter.com/mickaelbaron" target="_blank"
              >@mickaelbaron</a
            >)
          </p>
          <p>
            You want to contribute?
            <a
              href="https://github.com/mickaelbaron/mysharelatexmanager"
              target="_blank"
              >MySharelatexManager on Github</a
            >
          </p>
          <div class="alert alert-warning" role="alert">
            <b>Disclaimer</b>: MyShareLatexManager is a personnal tool hosted to
            my Github account. There is no affiliation with the company that
            publishes Overleaf/Sharelatex. Any issues related to the
            MyShareLatexManager tool should be reported on the
            MyShareLatexManager repository :
            <a
              target="_blank"
              href="https://github.com/mickaelbaron/mysharelatexmanager"
              >github.com/mickaelbaron/mysharelatexmanager</a
            >
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" @click="close()">
            Close
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
