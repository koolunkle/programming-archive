import sys

from lxml import etree  # type: ignore


def read_xpath(tree, xpath):
    tags = tree.xpath(xpath)
    if tags and len(tags) > 0:
        return True, tags[0]
    else:
        return False, None


def read_all(tree, xpath):
    for tag in tree:
        if len(tag) > 0:
            # 객체 또는 배열 요소인 경우
            read_all(tag, f"{xpath}/{tag.tag}")
        else:
            if tag.text:
                print(f"{xpath}/{tag.tag}={tag.text}")
            else:
                print(f"{xpath}/{tag.tag}")


def open_xml_file(filename):
    try:
        with open(filename, encoding="UTF-8") as file:
            return etree.parse(file, parser=etree.XMLParser(encoding="utf-8"))
    except (OSError, etree.XMLSyntaxError) as e:
        print(f"XML 데이터를 파싱하는 데 실패했습니다. 사유={e}")


# message1.xml 파일은 같은 디렉터리에 있어야 한다.
xml_tree = open_xml_file("message1.xml")

if not xml_tree:
    # 더 이상 로직을 진행할 수 없으므로 종료한다.
    sys.exit(0)

# Iterator 기반 접근
exist, root_tree = read_xpath(xml_tree, "/message")
assert root_tree is not None
read_all(root_tree, root_tree.tag)
