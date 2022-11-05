package cn.lingjiatong.re.auth.security.authorizationserver;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.exception.BusinessException;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ParamErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * 自定义oauth2异常处理器
 *
 * @author Ling, Jiatong
 * Date: 2022/10/25 22:58
 */
@Slf4j
@Component
public class OAuth2WebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    @Override
    public ResponseEntity<?> translate(Exception e) {
        log.error(e.toString(), e);
        if (e instanceof ParamErrorException) {
            ParamErrorException paramErrorException = (ParamErrorException) e;
            return new ResponseEntity<>(ResultVO.error(paramErrorException.getCode(), paramErrorException.getMessage()), HttpStatus.OK);
        } else if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            return new ResponseEntity<>(ResultVO.error(businessException.getCode(), businessException.getMessage()), HttpStatus.OK);
        } else if (e instanceof UsernameNotFoundException) {
            return new ResponseEntity<>(ResultVO.error(ErrorEnum.RESOURCE_NOT_EXIST_ERROR.getCode(), ErrorEnum.RESOURCE_NOT_EXIST_ERROR.getMessage()), HttpStatus.OK);
        } else if (e instanceof BadCredentialsException) {
            return new ResponseEntity<>(ResultVO.error(ErrorEnum.USERNAME_OR_PASSWORD_ERROR.getCode(), ErrorEnum.USERNAME_OR_PASSWORD_ERROR.getMessage()), HttpStatus.OK);
        } else if (e instanceof InvalidScopeException) {
            return new ResponseEntity<>(ResultVO.error(ErrorEnum.INVALID_SCOPE_ERROR.getCode(), ErrorEnum.INVALID_SCOPE_ERROR.getMessage()), HttpStatus.OK);
        } else if (e instanceof InvalidRequestException) {
            return new ResponseEntity<>(ResultVO.error(ErrorEnum.MISSING_GRANT_TYPE_ERROR.getCode(), ErrorEnum.MISSING_GRANT_TYPE_ERROR.getMessage()), HttpStatus.OK);
        } else if (e instanceof UnsupportedGrantTypeException) {
            return new ResponseEntity<>(ResultVO.error(ErrorEnum.UNSUPPORTED_GRANT_TYPE_ERROR.getCode(), ErrorEnum.UNSUPPORTED_GRANT_TYPE_ERROR.getMessage()), HttpStatus.OK);
        } else if (e instanceof InvalidTokenException) {
            return new ResponseEntity<>(ResultVO.error(ErrorEnum.INVALID_TOKEN_ERROR.getCode(), ErrorEnum.INVALID_TOKEN_ERROR.getMessage()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResultVO.error(ErrorEnum.UNKNOWN_ERROR.getCode(), ErrorEnum.UNKNOWN_ERROR.getMessage()), HttpStatus.OK);
        }
    }
}
