<?php
class DesCrypter
{
    private $key = 'sfe023f_';
    private $encrypter;

    public function __construct($key = '', $algorithm = MCRYPT_DES, $mode = MCRYPT_MODE_CBC)
    {
        if (!empty($key)) {
            $this->key = $key;
        }
        $this->encrypter = mcrypt_module_open($algorithm, '', $mode, '');
    }

    public function encrypt($origData)
    {
        $origData = pkcs5padding($origData, mcrypt_enc_get_block_size($this->encrypter));
        mcrypt_generic_init($this->encrypter, $this->key, substr($this->key, 0, 8));
        $ciphertext = mcrypt_generic($this->encrypter, $origData);
        mcrypt_generic_deinit($this->encrypter);
        return $ciphertext;
    }

    public function decrypt($ciphertext)
    {
        mcrypt_generic_init($this->encrypter, $this->key, substr($this->key, 0, 8));
        $origData = mdecrypt_generic($this->encrypter, $ciphertext);
        mcrypt_generic_deinit($this->encrypter);
        return pkcs5unPadding($origData);
    }

    public function close()
    {
        mcrypt_module_close($this->encrypter);
    }
}

function pkcs5padding($data, $blocksize)
{
    $padding = $blocksize - strlen($data) % $blocksize;
    $paddingText = str_repeat(chr($padding), $padding);
    return $data . $paddingText;
}

function pkcs5unPadding($data)
{
    $length = strlen($data);
    $unpadding = ord($data[$length - 1]);
    return substr($data, 0, $length - $unpadding);
}

// 3DES方式
$encrypter = new DesCrypter('sfe023f_sefiel#fi32lf3e!', MCRYPT_3DES);
// DES 方式
// $encrypter = new DesCrypter();
$data = 'polaris@studygolang';
$result = $encrypter->encrypt($data);
var_dump(base64_encode($result));
var_dump(rtrim($encrypter->decrypt($result)));
$encrypter->close();
