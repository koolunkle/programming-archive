// ES6 클래스 컴포넌트 변환 예시 

import { Component } from 'react';

export default class SimpleComponent extends Component {
    static defaultProps = {
        greeting: 'Hello',
    };

    render() {
        const { greeting, name } = this.props;
        return (
            <div>
                {greeting}, {name}!
            </div>
        )
    }
}

SimpleComponent.prototype = {
    name: PropTypes.string.isRequired,
}
