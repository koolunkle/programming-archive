from lxml import etree  # type: ignore


def open_xml_file(filename):
    try:
        with open(filename, encoding="UTF-8") as file:
            return etree.parse(file, parser=etree.XMLParser(encoding="utf-8"))
    except (OSError, etree.XMLSyntaxError) as e:
        print(f"XML 데이터를 파싱하는 데 실패했습니다. 사유={e}")


# message1.xml 파일은 같은 디렉터리에 있어야 한다.
xml_tree = open_xml_file("message1.xml")

if xml_tree:
    print(etree.tounicode(xml_tree, pretty_print=True))
