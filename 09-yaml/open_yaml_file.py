import yaml
import yaml.parser


def open_yaml_file(filename):
    try:
        with open(filename, encoding="UTF-8") as file:
            return yaml.load(file, Loader=yaml.SafeLoader)
    except yaml.parser.ParserError as e:
        print(f"YAML 데이터를 파싱하는 데 실패했습니다. 사유={e}")
        return None


# message1.yaml 파일은 같은 디렉터리에 있어야 한다.
# yaml_data = open_yaml_file("message1.yaml")
yaml_data = open_yaml_file("realapp_config2.yaml")

if yaml_data:
    print(yaml_data)
