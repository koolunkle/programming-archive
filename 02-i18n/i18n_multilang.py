import gettext

translation_ko = gettext.translation(
    domain="i18n_test", localedir="./locale", languages=["ko_KR"]
)

translation_zh = gettext.translation(
    domain="i18n_test", localedir="./locale", languages=["zh_CN"]
)

translation_ja = gettext.translation(
    domain="i18n_test", localedir="./locale", languages=["ja_JP"]
)

# 설정이 바뀔 때마다 다른 translation 오브젝트의 install() 함수를 호출한다.
# translation_ko.install()
# translation_zh.install()
# translation_ja.install()

# _ = translation_ko.gettext
_ = translation_zh.gettext
# _ = translation_ja.gettext

print(_("Test message 1"))
print("Test message 2")
print(_("Test message 3"))
