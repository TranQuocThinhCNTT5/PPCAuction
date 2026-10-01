document.addEventListener('submit', function (event) {
  const form = event.target;
  if (form.matches('[data-confirm]')) {
    const message = form.getAttribute('data-confirm') || 'Bạn có chắc muốn thực hiện thao tác này?';
    if (!window.confirm(message)) event.preventDefault();
  }
});

document.addEventListener('DOMContentLoaded', function () {
  document.querySelectorAll('[data-add-participant]').forEach(function (button) {
    button.addEventListener('click', function () {
      const template = document.querySelector('#participant-template');
      const target = document.querySelector('#participant-list');
      if (!template || !target) return;
      const index = target.querySelectorAll('[data-participant]').length;
      const html = template.innerHTML.replaceAll('__index__', index);
      target.insertAdjacentHTML('beforeend', html);
    });
  });
  document.addEventListener('click', function (event) {
    const button = event.target.closest('[data-remove-participant]');
    if (button) button.closest('[data-participant]').remove();
  });
});
