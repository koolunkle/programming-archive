import gettext

translation_ko = gettext.translation(
    domain="i18n_test", localedir="./locale", languages=["ko_KR"]
)

# translation_ko.install()

_ = translation_ko.gettext

print(_("Test message 1"))
print(_("Test message 2"))
print(_("Test message 3"))
