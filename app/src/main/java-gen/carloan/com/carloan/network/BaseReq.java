package carloan.com.carloan.network;

import carloan.com.carloan.constant.ActTypeEnum;
import carloan.com.carloan.constant.PlatformEnum;

/**
 * Created by jiahua on 17-2-24.
 * Description：发送请求的实体类
 */

public class BaseReq
{
    /**
     * 请求来源
     * @see PlatformEnum
     */
    private int from = PlatformEnum.APP_ANDROID.getValue();
    /**
     * 操作命令
     *@see ActTypeEnum
     */
    private int actType;

    public int getFrom()
    {
        return from;
    }

    public void setFrom(int from)
    {
        this.from = from;
    }

    public int getActType()
    {
        return actType;
    }

    public void setActType(int actType)
    {
        this.actType = actType;
    }

}
